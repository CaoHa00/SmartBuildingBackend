package com.example.SmartBuildingBackend.service.implementation;

import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.mapper.EquipmentMapper;
import com.example.SmartBuildingBackend.service.EquipmentService;
import com.example.SmartBuildingBackend.service.LogValueService;
import com.example.SmartBuildingBackend.service.TuyaService;
import com.example.SmartBuildingBackend.service.TuyaSignatureHelper;
import com.example.SmartBuildingBackend.service.ValueService;

@Service
public class TuyaServiceImplementation implements TuyaService {

    private TuyaSignatureHelper tuyaSignatureHelper;
    private EquipmentService equipmentService;
    private LogValueService logValueService;
    private ValueService valueService;
    private String clientId;
    private String secret;
    private String baseUrl;

    @Autowired
    public TuyaServiceImplementation(TuyaSignatureHelper tuyaSignatureHelper, EquipmentService equipmentService,
            ValueService valueService,
            LogValueService logValueService,
            @Value("${tuya.client.id}") String clientId, @Value("${tuya.secret}") String secret,
            @Value("${tuya.base.url}") String baseUrl) {
        this.tuyaSignatureHelper = tuyaSignatureHelper;
        this.clientId = clientId;
        this.secret = secret;
        this.baseUrl = baseUrl;
        this.equipmentService = equipmentService;
        this.logValueService = logValueService;
        this.valueService = valueService;

    }

    private String accessToken = null;
    private long tokenExpiry = 0;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAccessToken() {
        long currentTime = System.currentTimeMillis();
        if (accessToken == null || currentTime >= tokenExpiry) {
            fetchAccessToken();
        }
        return accessToken;
    }

    @Override
    public void fetchAccessToken() {
        long timestamp = System.currentTimeMillis();
        String nonce = tuyaSignatureHelper.generateNonce();
        String method = "GET";
        String body = ""; // Empty for GET requests
        String url = "/v1.0/token?grant_type=1"; // URL path

        String sign = tuyaSignatureHelper.generateSignature(clientId, secret, timestamp, nonce, method, body, url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("client_id", clientId);
        headers.set("sign", sign);
        headers.set("t", String.valueOf(timestamp));
        headers.set("nonce", nonce);
        headers.set("sign_method", "HMAC-SHA256");

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> bodyMap = response.getBody();
            if (bodyMap.containsKey("result")) {
                Map<String, Object> result = (Map<String, Object>) bodyMap.get("result");
                accessToken = (String) result.get("access_token");
                tokenExpiry = System.currentTimeMillis() + ((Integer) result.get("expire_time") * 1000);
                System.out.println("Access Token: " + accessToken);
            } else {
                System.out.println("Error: " + bodyMap.get("msg"));
            }
        } else {
            System.out.println("Failed to fetch token. HTTP Status: " + response.getStatusCode());
        }
    }

    @Override
    public String getDeviceProperty(Long equipmentId) {
        EquipmentDto equipmentDto = equipmentService.getEquipmentById(equipmentId);
        String deviceId = equipmentDto.getDeviceId();
        getAccessToken();
        String method = "GET";
        String body = "";
        String url = "/v2.0/cloud/thing/" + deviceId + "/shadow/properties"; // API Path

        ResponseEntity<String> response = getResponse(url, method, body);
        String responseBody = response.getBody();

        if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
            return extractPropertiesFromResponse(responseBody, equipmentDto);
        }
        return responseBody;
    }

    @Override
    public String extractPropertiesFromResponse(String responseBody, EquipmentDto equipmentDto) {
        JSONObject valueJson = new JSONObject();
        JSONObject sendJson = new JSONObject();
        Equipment equipment = EquipmentMapper.mapToEquipment(equipmentDto);
        double energyTotal = 0;
        Long time = (long) 0;
        try {

            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject result = jsonResponse.getJSONObject("result");
            JSONArray properties = result.getJSONArray("properties");
            time = jsonResponse.getLong("t");
            // Extract "phase_a" (which contains raw data for voltage, current, and power)
            for (int i = 0; i < properties.length(); i++) {
                JSONObject property = properties.getJSONObject(i);
                String code = property.getString("code");
                Object value = property.get("value");

                // Check for the specific codes we're interested in
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
                        // sent to front
                        sendJson.put("voltage", val.getDouble("voltage"));
                        sendJson.put("current", val.getDouble("current"));
                        sendJson.put("active_power", val.getDouble("power"));
                        break;
                    } else {
                        return "Invalid phase_a value format.";
                    }
                }
            }
            Iterator<String> keys = valueJson.keys();
            while (keys.hasNext()) {
                String keyName = keys.next(); // this is the key
                Long valueId = valueService.getValueByName(keyName);
                LogValueDto logValueDto = new LogValueDto();
                logValueDto.setTimeStamp(time);
                logValueDto.setValueResponse(valueJson.getDouble(keyName)); // or val.get(keyName)
                logValueService.addLogValue(equipment.getEquipmentId(), valueId, logValueDto);
            }

            sendJson.put("timestamp", jsonResponse.getLong("t"));

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
                                                                                                       // current //
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
        // getAccessToken(); // Ensure access token is valid
        long timestamp = System.currentTimeMillis();
        String nonce = tuyaSignatureHelper.generateNonce();

        // Include accessToken in the signature
        String sign = tuyaSignatureHelper.generateSignatureWithAccessToken(clientId, accessToken, secret, timestamp,
                nonce, method, body, url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("client_id", clientId);
        headers.set("access_token", accessToken);
        headers.set("sign", sign);
        headers.set("t", String.valueOf(timestamp));
        headers.set("nonce", nonce);
        headers.set("sign_method", "HMAC-SHA256");

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + url, HttpMethod.GET, request, String.class);
        return response;
    }

    @Override
    public String getListDevicesProperty() {
        getAccessToken();
        String[] devices = { "6cf85b50df5c642854bo2n" };
        String method = "GET";
        String body = "";
        String url = "/v2.0/cloud/thing/group/device/" + "6cf85b50df5c642854bo2n";

        ResponseEntity<String> response = getResponse(url, method, body);
        String responseBody = response.getBody();
        return responseBody;
    }

    @Override
    public LogValueDto addLogValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addLogValue'");
    }
}
