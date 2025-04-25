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
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

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
import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.dto.space.SpaceTuyaDto;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.space.Space;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.space.SpaceRepository;
import com.example.SmartBuildingBackend.service.equipment.EquipmentService;
import com.example.SmartBuildingBackend.service.equipment.LogValueService;
import com.example.SmartBuildingBackend.service.equipment.ValueService;
import com.example.SmartBuildingBackend.service.space.SpaceTuyaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TuyaServiceImplementation implements TuyaService {
    ;
    private final TuyaSignatureHelper tuyaSignatureHelper;
    private final EquipmentService equipmentService;
    private final ValueService valueService;
    private final LogValueService logValueService;
    private final SpaceTuyaService spaceTuyaService;
    private final EquipmentRepository equipmentRepository;
    private final SpaceRepository spaceRepository;

    @Value("${tuya.client.id}")
    private String clientId;

    @Value("${tuya.secret}")
    private String secret;

    @Value("${tuya.base.url}")
    private String baseUrl;

    private String accessToken = null;
    private long tokenExpiry = 0;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAccessToken(String url, String method, String body) {
        long currentTime = System.currentTimeMillis();
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
    public String getDeviceProperty(UUID equipmentId) {
        EquipmentDto equipmentDto = equipmentService.getEquipmentById(equipmentId);
        String deviceId = equipmentDto.getDeviceId();
        String method = "GET";
        String body = "";
        String url = "/v2.0/cloud/thing/" + deviceId + "/shadow/properties"; // API Path

        ResponseEntity<String> response = getResponse(url, method, body);
        String responseBody = response.getBody();
        // electric
        if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
            return extractPropertiesFromResponse(responseBody, equipmentDto);
        }
        return responseBody;
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
        Space space = spaceRepository.findById(spaceId).orElseThrow(() -> new NoSuchElementException("Space with ID " + spaceId + " not found"));;
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
                        result.append("Failed to parse Tuya response JSON: ").append(e.getMessage()).append("\n\n");
                        e.printStackTrace();
                    }
                } else {
                    result.append("Request failed or empty response.\n\n");
                }
            }
        }

        return result.toString();
    }

    @Override // not clean yet
    public String getLatestStatusDeviceList(List<Equipment> equipments) {
        String baseUrl = "/v1.0/iot-03/devices/status";

        List<String> deviceIds = equipments.stream()
                .map(Equipment::getDeviceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (deviceIds.isEmpty())
            return "[]";

        String queryParam = String.join(",", deviceIds);
        String fullUrl = baseUrl + "?device_ids=" + UriUtils.encodeQueryParam(queryParam, StandardCharsets.UTF_8);

        ResponseEntity<String> response = getResponse(fullUrl, "GET", "");
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(response.getBody());
            if (root.path("success").asBoolean()) {
                JsonNode results = root.path("result");

                for (JsonNode device : results) {
                    String deviceId = device.path("id").asText();
                    Equipment equipment = equipmentRepository.findByDeviceId(deviceId);
                    JsonNode statuses = device.path("status");
                    boolean lightSwitch1=false;
                    for (JsonNode status : statuses) {
                        String code = status.path("code").asText();
                        JsonNode valueNode = status.path("value");

                        UUID valueId = null;
                        
                        boolean lightSwitch2;
                        switch (code) {
                            case "va_temperature": {
                                valueId = valueService.getValueByName("temperature");
                                double temperature = valueNode.asInt() / 10.0;
                                saveLog(equipment, valueId, temperature);
                                break;
                            }
                            case "va_humidity": {
                                valueId = valueService.getValueByName("humidity");
                                double humidity = valueNode.asInt();
                                saveLog(equipment, valueId, humidity);
                                break;
                            }
                            case "battery_percentage": {
                                valueId = valueService.getValueByName("battery-percentage");
                                double battery = valueNode.asDouble();
                                saveLog(equipment, valueId, battery);
                                break;
                            }
                            case "forward_energy_total": {
                                valueId = valueService.getValueByName("forward-energy");
                                double energyTotal = valueNode.asDouble() / 100.0;
                                saveLog(equipment, valueId, energyTotal);
                                break;
                            }
                            case "charge_energy": {
                                valueId = valueService.getValueByName("charge-energy");
                                double chargeEnergy = valueNode.asDouble();
                                saveLog(equipment, valueId, chargeEnergy);
                                break;
                            }
                            case "balance_energy": {
                                valueId = valueService.getValueByName("balance-energy");
                                double balanceEnergy = valueNode.asDouble();
                                saveLog(equipment, valueId, balanceEnergy);
                                break;
                            }
                            case "switch_1": {
                                lightSwitch1 = valueNode.asBoolean();  
                                break;
                            }
                            case "switch_2": {
                                valueId = valueService.getValueByName("light-status");
                                lightSwitch2 = valueNode.asBoolean();
                                if (lightSwitch1 || lightSwitch2) {
                                    saveLog(equipment, valueId, 1.0);
                                } else {
                                    saveLog(equipment, valueId, 0.0);
                                }    
                                break;
                            }
                            case "phase_a": {
                                if (valueNode.isTextual()) {
                                    try {
                                        String rawJson = valueNode.asText(); // Get the inner JSON string
                                        JSONObject val = new JSONObject(rawJson);
                                        double voltage = val.getDouble("voltage");
                                        double current = val.getDouble("electricCurrent");
                                        double power = val.getDouble("power");

                                        UUID voltageId = valueService.getValueByName("voltage");
                                        UUID currentId = valueService.getValueByName("electricCurrent");
                                        UUID powerId = valueService.getValueByName("active-power");

                                        saveLog(equipment, voltageId, voltage);
                                        saveLog(equipment, currentId, current);
                                        saveLog(equipment, powerId, power);

                                    } catch (JSONException e) {
                                        System.err.println("Invalid phase_a format: " + e.getMessage());
                                    }
                                }
                                break;
                            }
                            default:
                                System.out.println("Unhandled code: " + code);
                        }
                    }
                    System.out.println("Processed device ID: " + deviceId);
                }
            } else {
                System.out.println("Tuya API response indicates failure.");
            }
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse Tuya response JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return response.getBody();
    }

    // this method to save log value
    private void saveLog(Equipment equipment, UUID valueId, double value) {
        if (valueId == null || equipment == null)
            return;

        LogValueDto logValueDto = new LogValueDto();
        logValueDto.setTimeStamp(System.currentTimeMillis());
        logValueDto.setValueResponse(value);
        logValueService.addLogValue(equipment.getEquipmentId(), valueId, logValueDto);
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

    @Override
    public String extractPropertiesFromResponse(String responseBody, EquipmentDto equipmentDto) {
        JSONObject valueJson = new JSONObject();
        JSONObject sendJson = new JSONObject();
        double energyTotal = 0;
        long timestamp = 0;

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject result = jsonResponse.getJSONObject("result");

            JSONArray properties = result.getJSONArray("properties");
            timestamp = jsonResponse.getLong("t");

            for (int i = 0; i < properties.length(); i++) {
                JSONObject property = properties.getJSONObject(i);
                String code = property.getString("code");
                Object value = property.get("value");

                if ("forward_energy_total".equals(code)) {
                    energyTotal = ((Number) value).doubleValue() / 100.0;
                    valueJson.put("total power", energyTotal);
                    sendJson.put("forward_energy_power", energyTotal);
                } else if ("phase_a".equals(code)) {
                    if (value instanceof String) {
                        JSONObject val = parsePhaseA((String) value);
                        valueJson.put("voltage", val.getDouble("voltage"));
                        valueJson.put("current", val.getDouble("current"));
                        valueJson.put("active power", val.getDouble("power"));

                        sendJson.put("voltage", val.getDouble("voltage"));
                        sendJson.put("current", val.getDouble("current"));
                        sendJson.put("active_power", val.getDouble("power"));
                    } else {
                        return "Invalid phase_a value format.";
                    }
                }
            }

            UUID equipmentId = equipmentDto.getEquipmentId();
            Iterator<String> keys = valueJson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                UUID valueId = valueService.getValueByName(key); // assumes this does a lookup
                LogValueDto logValueDto = new LogValueDto();
                logValueDto.setTimeStamp(timestamp);
                logValueDto.setValueResponse(valueJson.getDouble(key));

                logValueService.addLogValue(equipmentId, valueId, logValueDto);
            }

            sendJson.put("timestamp", timestamp);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing response: " + e.getMessage();
        }
        return sendJson.toString();
    }

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

    @Override
    public LogValueDto addLogValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addLogValue'");
    }

}
