package com.example.SmartBuildingBackend.service.provider.tuya;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;
import org.springframework.http.MediaType;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentDto;
import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;
import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.dto.space.SpaceTuyaDto;
import com.example.SmartBuildingBackend.dto.tuyaResponse.TuyaProperty;
import com.example.SmartBuildingBackend.dto.tuyaResponse.TuyaResponse;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;
import com.example.SmartBuildingBackend.entity.equipment.LogValue;
import com.example.SmartBuildingBackend.entity.equipment.Value;
import com.example.SmartBuildingBackend.entity.space.Space;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.space.SpaceRepository;
import com.example.SmartBuildingBackend.service.equipment.EquipmentService;
import com.example.SmartBuildingBackend.service.equipment.EquipmentStateService;
import com.example.SmartBuildingBackend.service.equipment.LogValueService;
import com.example.SmartBuildingBackend.service.equipment.ValueService;
import com.example.SmartBuildingBackend.service.space.SpaceTuyaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class TuyaServiceImplementation implements TuyaService {
    private com.example.SmartBuildingBackend.entity.equipment.Value value;
    private final TuyaSignatureHelper tuyaSignatureHelper;
    private final EquipmentService equipmentService;
    private final EquipmentStateService equipmentStateService;
    private final ValueService valueService;
    private final LogValueService logValueService;
    private final SpaceTuyaService spaceTuyaService;
    private final EquipmentRepository equipmentRepository;
    private final SpaceRepository spaceRepository;
    private final TuyaProperties tuyaProperties;

    private String accessToken = null;
    private long tokenExpiry = 0;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String getAccessToken(String url, String method, String body) {
        long currentTime = System.currentTimeMillis();

        String baseUrl = tuyaProperties.getBaseUrl();
        if (accessToken == null || currentTime >= tokenExpiry) {
            url = "/v1.0/token?grant_type=1";
            HttpEntity<String> requestEntity = buildAuthorizedRequest(url, "GET", "");

            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + url, HttpMethod.GET, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode root = objectMapper.readTree(response.getBody());

                    String token = root.path("result").path("access_token").asText();
                    long expiresIn = root.path("result").path("expire_time").asLong();

                    this.accessToken = token;
                    this.tokenExpiry = System.currentTimeMillis() + expiresIn - 60_000; // 1 min buffer

                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse token response", e);
                }
            } else {
                throw new RuntimeException("Failed to fetch access token: " + response.getStatusCode());
            }
        }
        return accessToken;
    }

    private HttpEntity<String> buildAuthorizedRequest(String url, String method, String body) {
        HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
        String clientId = tuyaProperties.getClientId();
        String secret = tuyaProperties.getSecret();
        if (httpMethod == null) {
            throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
        long timestamp = System.currentTimeMillis();
        String nonce = tuyaSignatureHelper.generateNonce();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("client_id", clientId);
        String sign = "";
        if (!url.contains("token")) {
            headers.set("access_token", accessToken);
            sign = tuyaSignatureHelper.generateSignatureWithAccessToken(
                    clientId, accessToken, secret, timestamp, nonce, method, body, url);
        } else {
            sign = tuyaSignatureHelper.generateSignature(
                    clientId, secret, timestamp, nonce, method, body, url);
        }
        headers.set("sign", sign);
        headers.set("t", String.valueOf(timestamp));
        headers.set("nonce", nonce);
        headers.set("sign_method", "HMAC-SHA256");

        return new HttpEntity<>(body, headers);
    }

    @Override
    public String getElevatorElectric(List<Equipment> electricElevators,
            List<EquipmentState> electricElevatorStates,
            Long timeStamp,
            List<Value> values) {
        if (electricElevators.isEmpty()) {
            return "No elevator found";
        }

        Map<String, Value> valueMap = new HashMap<>();
        for (Value value : values) {
            valueMap.put(value.getValueName(), value);
        }

        List<EquipmentState> savedEquipmentStates = new ArrayList<>();

        for (Equipment electricElevator : electricElevators) {
            String deviceId = electricElevator.getDeviceId();
            String method = "GET";
            String body = "";
            String codes = "VoltageA,VoltageB,VoltageC,TotalEnergyConsumed,Current,ActivePower,Temperature";
            String url = "/v2.0/cloud/thing/" + deviceId + "/shadow/properties?codes=" + codes;
            ResponseEntity<String> response = getResponse(url, method, body);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                if (!rootNode.path("success").asBoolean()) {
                    System.out.println("Failed to fetch data for deviceId: " + deviceId);
                    continue;
                }

                JsonNode properties = rootNode.path("result").path("properties");
                double voltageSum = 0;
                double voltageAvg = 0;

                for (JsonNode property : properties) {
                    String code = property.path("code").asText();
                    double value = property.path("value").asDouble();
                    

                    if (code.equals("VoltageA") || code.equals("VoltageB") || code.equals("VoltageC")) {
                        voltageSum += value;
                    }

                    if (code.equals("VoltageC")) {
                        voltageAvg = (voltageSum / 3)/10.0;
                        saveOrUpdateState("voltage", voltageAvg, electricElevator, timeStamp, valueMap,
                                electricElevatorStates, savedEquipmentStates);
                                continue;
                    }

                    Map<String, String> codeToValueKey = Map.of(
                            "TotalEnergyConsumed", "total-energy-consumed",
                            "Current", "electric-current",
                            "ActivePower", "active-power",
                            "Temperature", "temperature");

                    if (codeToValueKey.containsKey(code)) {
                        saveOrUpdateState(codeToValueKey.get(code), value, electricElevator, timeStamp, valueMap,
                                electricElevatorStates, savedEquipmentStates);
                    }
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to process Tuya response", e);
            }

            System.out.println("Finished processing deviceId: " + deviceId);
        }
        equipmentStateService.saveAll(savedEquipmentStates);

        return "Elevator Logs Added Success";
    }

    private void saveOrUpdateState(String valueKey,
            double responseValue,
            Equipment electricElevator,
            Long timeStamp,
            Map<String, Value> valueMap,
            List<EquipmentState> existingStates,
            List<EquipmentState> savedStates) {
        if(valueKey.equals("active-power")||valueKey.equals("electric-current") ){
            responseValue/=1000;
        }
        if(valueKey.equals("total-energy-consumed")){
            responseValue/=100;
        }
        if(valueKey.equals("temperature")){
            responseValue/=10;
        }
        Value value = valueMap.get(valueKey);
        if (value == null)
            return;

        Map<UUID, EquipmentState> equipmentStateMap = existingStates.stream()
                .filter(es -> es.getValue() != null && es.getValue().getValueId().equals(value.getValueId())) // findByValue
                .collect(Collectors.toMap(
                        es -> es.getEquipment().getEquipmentId(),
                        Function.identity()));

        EquipmentState equipmentState = equipmentStateMap.get(electricElevator.getEquipmentId());// findByEquipment
        if (equipmentState == null) {
            equipmentState = new EquipmentState();
            equipmentState.setEquipment(electricElevator);
            equipmentState.setValue(value);
        }else{
        }

        equipmentState.setValueResponse(responseValue);
        equipmentState.setTimeStamp(timeStamp);
        savedStates.add(equipmentState);
    }

    @Override // Not use
    public String createTuyaSpace(String spaceName, UUID spaceId) {
        String url = "/v2.0/cloud/space/creation";
        String body = "{\"name\":\"" + spaceName + "\"}";
        ResponseEntity<String> response = getResponse(url, "POST", body);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String spaceTuyaPlatformId = getResponseSpaceIdFromBody(response.getBody());

            SpaceTuyaDto dto = new SpaceTuyaDto();
            dto.setSpaceId(spaceId);
            dto.setSpaceTuyaPlatFormId(Long.parseLong(spaceTuyaPlatformId));
            dto.setSpaceTuyaName(spaceName);

            spaceTuyaService.addSpaceTuya(dto);
        }
        return response.getBody();
    }

    private String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    @Override
    public String createTuyaGroup(String spaceTuyaPlatformId, String groupName) {
        String url = "/v2.1/cloud/thing/group";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("space_id", spaceTuyaPlatformId);
        requestBody.put("group_name", groupName);
        requestBody.put("device_ids", new ArrayList<>()); // empty list

        String jsonBody = toJson(requestBody);
        ResponseEntity<String> response = getResponse(url, "POST", jsonBody);
        return response.getBody();
    }

    @Override
    public String controlLight(UUID spaceId, int valueLight) {
        StringBuilder result = new StringBuilder();
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new NoSuchElementException("Space with ID " + spaceId + " not found"));
        ;
        List<Equipment> listEquipments = space.getEquipments();
        // Determine the value for light control (on or off)
        String valueLightString = (valueLight == 0) ? "false" : "true";

        for (Equipment equipment : listEquipments) {
            if (equipment.getEquipmentType().getEquipmentTypeName().equals("Tuya")
                    && equipment.getCategory().getCategoryName().equals("switch")) { // make sure it is switch of Tuya
                String baseUrl = "/v1.0/iot-03/devices/" + equipment.getDeviceId() + "/commands";
                String body = "{\"commands\": [";
                // turn on or turn off ALL
                body += "{\"code\": \"switch_1\", \"value\": " + valueLightString + "}, " +
                        "{\"code\": \"switch_2\", \"value\": " + valueLightString + "}";
                body += "]}";

                // Log request
                result.append("Equipment ID: ").append(equipment.getDeviceId()).append("\n");
                result.append("Request Body: ").append(body).append("\n");

                // Send request
                ResponseEntity<String> response = getResponse(baseUrl, "POST", body);
                ObjectMapper mapper = new ObjectMapper();

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    try {
                        JsonNode root = mapper.readTree(response.getBody());
                        boolean success = root.path("result").asBoolean(false);
                        result.append("Response Success: ").append(success).append("\n\n");
                    } catch (JsonProcessingException e) {
                        System.err.println("Failed to parse Tuya response JSON: " + e.getMessage());
                        throw new RuntimeException("Failed to process Tuya response", e);
                    }
                } else {
                    result.append("Request failed or empty response.\n\n");
                }
            }
        }
        return result.toString();
    }

    @Override
    public String getStatusLight(List<Equipment> equipments, List<EquipmentState> equipmentStates, Long timeStamp,
            Value valueInput) {

        String baseUrl = "/v1.0/iot-03/devices/status";

        List<String> deviceIds = equipments.stream()
                .map(Equipment::getDeviceId)
                .filter(Objects::nonNull)
                .toList();

        if (deviceIds.isEmpty())
            return "[]";

        String queryParam = String.join(",", deviceIds);
        String fullUrl = baseUrl + "?device_ids=" + UriUtils.encodeQueryParam(queryParam, StandardCharsets.UTF_8);

        ResponseEntity<String> response = getResponse(fullUrl, "GET", "");
        // List<LogValue> logValues = new ArrayList<>(); // save will be coded later
        try {
            JsonNode root = mapper.readTree(response.getBody());
            if (!root.path("success").asBoolean()) {
                System.out.println("Tuya API response indicates failure.");
                return response.getBody();
            }
            JsonNode resultArray = root.path("result");

            Map<String, Equipment> equipmentMap = equipments.stream()
                    .collect(Collectors.toMap(Equipment::getDeviceId, Function.identity()));
            Map<UUID, EquipmentState> equipmentStateMap = equipmentStates.stream()
                    .filter(es -> es.getValue() != null && es.getValue().getValueId().equals(valueInput.getValueId()))
                    .collect(Collectors.toMap(
                            es -> es.getEquipment().getEquipmentId(), // Key: Equipment ID
                            Function.identity() // Value: The EquipmentState
                    ));
            List<EquipmentState> savedEquipmentStates = new ArrayList<>();

            for (JsonNode deviceStatus : resultArray) {
                String deviceId = deviceStatus.path("id").asText();
                JsonNode properties = deviceStatus.path("status"); // This may vary depending on actual Tuya structure

                Equipment equipment = equipmentMap.get(deviceId);
                if (equipment == null) {
                    System.out.println("No matching Equipment for deviceId: " + deviceId);
                    continue;
                }

                boolean switch1 = false;
                boolean switch2 = false;

                for (JsonNode property : properties) {
                    String code = property.path("code").asText();
                    boolean valueCheck = property.path("value").asBoolean();

                    if ("switch_1".equals(code)) {
                        switch1 = valueCheck;
                    } else if ("switch_2".equals(code)) {
                        switch2 = valueCheck;
                        break;
                    }
                }

                Double status = (!switch1 && !switch2) ? 0.0 : 1.0;

                EquipmentState equipmentState = equipmentStateMap.get(equipment.getEquipmentId());

                if (equipmentState == null) {
                    // Create or reset the EquipmentState if value is null or doesn't match
                    equipmentState = new EquipmentState();
                    equipmentState.setEquipment(equipment);
                    equipmentState.setValue(valueInput);
                    equipmentState.setValueResponse(status);
                    equipmentState.setTimeStamp(timeStamp);
                    savedEquipmentStates.add(equipmentState);
                }else if (!equipmentState.getValueResponse().equals(status)) { // if #value -> add
                    equipmentState.setValueResponse(status);
                    equipmentState.setTimeStamp(timeStamp);
                    savedEquipmentStates.add(equipmentState);
                }

           
               
            }
            equipmentStateService.saveAll(savedEquipmentStates);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse Tuya response JSON: " + e.getMessage());
            throw new RuntimeException("Failed to process Tuya response", e);
        }

        return response.getBody();
    }

    public Double getFormattedTemperature(JsonNode statusArray) {
        for (JsonNode status : statusArray) {
            String code = status.path("code").asText();
            if ("va_temperature".equals(code)) {
                int rawValue = status.path("value").asInt(); // e.g., 266
                return rawValue / 10.0; // returns 26.6
            }
        }
        return null; // or throw exception if not found
    }

    @Override
    public String getResponseSpaceIdFromBody(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        if (!json.has("result")) {
            throw new IllegalArgumentException("Missing 'result' in Tuya response");
        }
        Object resultObj = json.get("result");
        if (resultObj instanceof JSONObject) {
            JSONObject result = (JSONObject) resultObj;
            return result.getString("space_id");
        } else {
            return resultObj.toString(); // Safely return numeric/string space ID
        }
    }

    // @Override
    // public String extractPropertiesFromResponse(String responseBody, EquipmentDto
    // equipmentDto) {
    // JSONObject valueJson = new JSONObject();
    // JSONObject sendJson = new JSONObject();
    // double energyTotal = 0;
    // long timestamp = 0;

    // try {
    // JSONObject jsonResponse = new JSONObject(responseBody);
    // JSONObject result = jsonResponse.getJSONObject("result");

    // JSONArray properties = result.getJSONArray("properties");
    // timestamp = jsonResponse.getLong("t");

    // for (int i = 0; i < properties.length(); i++) {
    // JSONObject property = properties.getJSONObject(i);
    // String code = property.getString("code");
    // Object value = property.get("value");

    // if ("forward_energy_total".equals(code)) {
    // energyTotal = ((Number) value).doubleValue() / 100.0;
    // valueJson.put("total power", energyTotal);
    // sendJson.put("forward_energy_power", energyTotal);
    // } else if ("phase_a".equals(code)) {
    // if (value instanceof String) {
    // JSONObject val = parsePhaseA((String) value);
    // valueJson.put("voltage", val.getDouble("voltage"));
    // valueJson.put("current", val.getDouble("current"));
    // valueJson.put("active power", val.getDouble("power"));

    // sendJson.put("voltage", val.getDouble("voltage"));
    // sendJson.put("current", val.getDouble("current"));
    // sendJson.put("active_power", val.getDouble("power"));
    // } else {
    // return "Invalid phase_a value format.";
    // }
    // }
    // }

    // UUID equipmentId = equipmentDto.getEquipmentId();
    // Iterator<String> keys = valueJson.keys();
    // while (keys.hasNext()) {
    // String key = keys.next();
    // if(key!=null){
    // Value value = valueService.getValueByName(key); // assumes this does a lookup
    // }

    // LogValueDto logValueDto = new LogValueDto();
    // logValueDto.setTimeStamp(timestamp);
    // logValueDto.setValueResponse(valueJson.getDouble(key));
    // logValueDto.setEquipmentId(equipmentId);
    // logValueDto.setLogValueId(value.getValueId());
    // logValueService.addLogValue(logValueDto);
    // }

    // sendJson.put("timestamp", timestamp);

    // } catch (Exception e) {
    // e.printStackTrace();
    // return "Error processing response: " + e.getMessage();
    // }
    // return sendJson.toString();
    // }

    @Override
    public JSONObject parsePhaseA(String base64Value) {
        JSONObject jsonResponse = new JSONObject();
        try {
            // Decode the Base64 encoded value
            byte[] decodedBytes = Base64.getDecoder().decode(base64Value);

            // Extract voltage, current, and power
            int voltage = (decodedBytes[0] << 8) | (decodedBytes[1] & 0xFF); // 2 bytes for voltage
            int current = (decodedBytes[2] << 16) | (decodedBytes[3] << 8) | (decodedBytes[4] & 0xFF); // 3 bytes for
                                                                                                       // current
            int power = (decodedBytes[5] << 16) | (decodedBytes[6] << 8) | (decodedBytes[7] & 0xFF); // 3 bytes for
                                                                                                     // power
            // Convert current and power to the required units
            double voltageInV = Math.round(voltage * 0.1 * 10.0) / 10.0; // Round to 1 decimal place
            double currentInA = Math.round(current * 0.001 * 100.0) / 100.0; // Round to 2 decimal places
            double powerInKW = Math.round(power * 0.001 * 100.0) / 100.0; // Round to 2 decimal places
            jsonResponse.put("voltage", voltageInV);
            jsonResponse.put("current", currentInA);
            jsonResponse.put("power", powerInKW);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return jsonResponse;
    }

    @Override
    public ResponseEntity<String> getResponse(String url, String method, String body) {
        getAccessToken(url, "GET", "");
        HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
        HttpEntity<String> request = buildAuthorizedRequest(url, method, body);
        String baseUrl = tuyaProperties.getBaseUrl();
        return restTemplate.exchange(baseUrl + url, httpMethod, request, String.class);
    }

    @Override
    public String getListDevicesProperty() {
        String method = "GET";
        String body = "";
        String url = "/v2.0/cloud/thing/group/device/6cf85b50df5c642854bo2n"; // API Path for single device
        getAccessToken(url, method, body);
        ResponseEntity<String> response = getResponse(url, method, body);
        return response.getBody();
    }

}
