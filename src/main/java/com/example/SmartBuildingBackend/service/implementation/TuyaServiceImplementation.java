package com.example.SmartBuildingBackend.service.implementation;

import java.util.Base64;
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
import com.example.SmartBuildingBackend.mapper.EquipmentMapper;
import com.example.SmartBuildingBackend.service.EquipmentService;
import com.example.SmartBuildingBackend.service.TuyaService;
import com.example.SmartBuildingBackend.service.TuyaSignatureHelper;

@Service
public class TuyaServiceImplementation implements TuyaService {

    private TuyaSignatureHelper tuyaSignatureHelper;
    private EquipmentService equipmentService;
    private String clientId;
    private String secret;
    private String baseUrl;

    // @Value("${tuya.client.id}")
    // private String CLIENT_ID;

    // @Value("${tuya.secret}")
    // private String SECRET;

    // @Value("${tuya.base.url}")
    // private String BASE_URL;

    @Autowired
    public TuyaServiceImplementation(TuyaSignatureHelper tuyaSignatureHelper, EquipmentService equipmentService,
            @Value("${tuya.client.id}") String clientId, @Value("${tuya.secret}") String secret,
            @Value("${tuya.base.url}") String baseUrl) {
        this.tuyaSignatureHelper = tuyaSignatureHelper;
        this.clientId = clientId;
        this.secret = secret;
        this.baseUrl = baseUrl;
        this.equipmentService = equipmentService;

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
        EquipmentDto equipment = equipmentService.getEquipmentById(equipmentId);
        String deviceId = equipment.getDeviceId();
        getAccessToken();
        String method = "GET";
        String body = "";
        String url = "/v2.0/cloud/thing/" + deviceId + "/shadow/properties"; // API Path

        ResponseEntity<String> response = getResponse(url, method, body);
        String responseBody = response.getBody();

        if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
            return extractPropertiesFromResponse(responseBody);
        }
        return responseBody;
    }

    @Override
    public String extractPropertiesFromResponse(String responseBody) {
        try {

            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject result = jsonResponse.getJSONObject("result");
            JSONArray properties = result.getJSONArray("properties");

            // Extract "phase_a" (which contains raw data for voltage, current, and power)
            for (int i = 0; i < properties.length(); i++) {
                JSONObject property = properties.getJSONObject(i);
                String code = property.getString("code");
                Object value = property.get("value");

                // Check for the specific codes we're interested in
                if ("phase_a".equals(code)) {
                    if (value instanceof String) {
                        return parsePhaseA((String) value); // Parse and return the extracted values
                    } else {
                        return "Invalid phase_a value format.";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing response: " + e.getMessage();
        }
        return "No phase_a data found";
    }

    @Override
    public String parsePhaseA(String base64Value) {
        try {
            // Decode the Base64 encoded value
            byte[] decodedBytes = Base64.getDecoder().decode(base64Value);

            // Assuming the decoded data format: [voltage (2 bytes), current (3 bytes),
            // power (3 bytes)]
            // Example: A phase_a string like "CPUAO7oACNo=" is decoded into byte array

            // Extract voltage, current, and power
            int voltage = (decodedBytes[0] << 8) | (decodedBytes[1] & 0xFF); // 2 bytes for voltage
            int current = (decodedBytes[2] << 16) | (decodedBytes[3] << 8) | (decodedBytes[4] & 0xFF); // 3 bytes for //
                                                                                                       // current
            int power = (decodedBytes[5] << 16) | (decodedBytes[6] << 8) | (decodedBytes[7] & 0xFF); // 3 bytes for
                                                                                                     // power

            // Convert current and power to the required units
            // double voltageInV = Math.round(voltage * 0.1 * 10.0) / 10.0; // Round to 1
            // decimal place
            // double currentInA = Math.round(current * 0.001 * 100.0) / 100.0; // Round to
            // 2 decimal places
            double powerInKW = Math.round(power * 0.001 * 100.0) / 100.0; // Round to 2 decimal places

            JSONObject jsonResponse = new JSONObject();
            // jsonResponse.put("voltage", voltageInV);
            // jsonResponse.put("current", currentInA);
            jsonResponse.put("power", powerInKW);

            return jsonResponse.toString(); // Return JSON as a string
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing phase_a data: " + e.getMessage();
        }
    }

    @Override
    public ResponseEntity<String> getResponse(String url, String method, String body) {
        // getAccessToken(); // Ensure access token is valid
        long timestamp = System.currentTimeMillis();
        String nonce = tuyaSignatureHelper.generateNonce();

        // 🔥 Include accessToken in the signature
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
    public LogValueDto addLogValue(LogValueDto logValueDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addLogValue'");
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

}
