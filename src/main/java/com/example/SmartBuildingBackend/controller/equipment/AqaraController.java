package com.example.SmartBuildingBackend.controller.equipment;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.entity.AqaraConfig;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.mapper.AqaraConfigMapper;
import com.example.SmartBuildingBackend.repository.AqaraConfigRepository;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.service.provider.aqara.AqaraService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/aqara")
public class AqaraController {
    private final AqaraService aqaraService;
    private final EquipmentRepository equipmentRepository;
    private AqaraConfigRepository aqaraConfigRepository;

    @PostMapping("/query-device-info")
    public ResponseEntity<String> queryDeviceInfo(@RequestBody Map<String, Object> requestBody) throws Exception {
        String response = aqaraService.sendRequestToAqara(requestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/query-resource-info")
    public ResponseEntity<String> queryResourceInfo(@RequestBody Map<String, Object> requestBody) {
        try {
            // Ensure "data" is present in the request body
            if (!requestBody.containsKey("data")) {
                return ResponseEntity.badRequest().body("Error: 'data' object is missing in the request body.");
            }

            // Extract "model" and "resourceId" from the "data" object
            Object dataObj = requestBody.get("data");
            if (!(dataObj instanceof Map)) {
                return ResponseEntity.badRequest().body("Error: 'data' format is incorrect.");
            }

            Map<String, Object> dataMap = (Map<String, Object>) dataObj;
            String model = (String) dataMap.get("model");
            String resourceId = (String) dataMap.get("resourceId");

            if (model == null || model.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'model' is required.");
            }

            // Call the service method
            String response = aqaraService.queryDetailsAttributes(requestBody, model, resourceId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // @PostMapping("/currentValue")
    // public ResponseEntity<String> queryAttributes(@RequestParam UUID equipmentId)
    // {
    // try {
    // // get Response from Chinese server
    // String response = aqaraService.queryTemparatureAttributes(equipmentId);
    // Long value = (long) 0;
    // // directly get the processed JSON response
    // ObjectNode processedJson = aqaraService.getJsonAPIFromServer(response,
    // equipmentId,value);
    // String updatedResponse = new
    // ObjectMapper().writeValueAsString(processedJson);
    // return ResponseEntity.ok(updatedResponse);
    // } catch (Exception e) {
    // return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
    // }
    // }

    @PostMapping("/light-control")
    public ResponseEntity<String> controlLight(@RequestParam Long value,@RequestParam Long buttonPosition, @RequestBody UUID equipmentId)
            throws Exception {
        String response = aqaraService.queryLightControl(equipmentId, value, buttonPosition);
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow();
        List<Equipment> equipmentList = Collections.singletonList(equipment);
        ObjectNode processedJson = aqaraService.processJsonAPIFromServer(response, equipmentList, value);
        String updatedResponse = new ObjectMapper().writeValueAsString(processedJson);
        return ResponseEntity.ok(updatedResponse);
    }
    @PostMapping("/authorization-verification-code")
    public ResponseEntity<String> authorizationVerificationCode() {
        try {
            String response = aqaraService.authorizationVerificationCode();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create-virtual-account")
    public ResponseEntity<String> createVirtualAccount() {
        try {
            String response = aqaraService.createVirtualAccount();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/obtain-access-token")
    public ResponseEntity<String> obtainAccessToken() {
        try {
            String response = aqaraService.ObtainAccessToken();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            // lấy dữ liệu ra từ database
            AqaraConfig aqaraConfig = aqaraConfigRepository.findFirstByOrderByAqaraConfigIdDesc()
                    .orElseThrow(() -> new RuntimeException("No Aqara configuration found in the database"));

            if (rootNode.has("result")) {
                JsonNode resultNode = rootNode.get("result");

                // Extract tokens safely
                String newAccessToken = resultNode.has("accessToken") ? resultNode.get("accessToken").asText() : null;
                String newRefreshToken = resultNode.has("refreshToken") ? resultNode.get("refreshToken").asText()
                        : null;

                if (newAccessToken != null && newRefreshToken != null) {

                    aqaraConfig.setAccessToken(newAccessToken);
                    aqaraConfig.setRefreshToken(newRefreshToken);
                    // cập nhập dữ liệu lại vào trong database
                    AqaraConfig updateAqaraConfig = aqaraConfigRepository.save(aqaraConfig);
                    AqaraConfigMapper.mapToAqaraConfigDto(updateAqaraConfig);
                    System.out.println(" Access Token Updated: " + newAccessToken);
                    System.out.println(" Refresh Token Updated: " + newRefreshToken);
                } else {
                    System.err.println("Missing accessToken or refreshToken in response.");

                }
            } else {
                System.err.println("'result' field is missing in the API response.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshTokenAccount() {
        try {
            String response = aqaraService.refreshToken();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            // lấy dữ liệu ra từ database
            AqaraConfig aqaraConfig = aqaraConfigRepository.findFirstByOrderByAqaraConfigIdDesc()
                    .orElseThrow(() -> new RuntimeException("No Aqara configuration found in the database"));

            if (rootNode.has("result")) {
                JsonNode resultNode = rootNode.get("result");

                // Extract tokens safely
                String newAccessToken = resultNode.has("accessToken") ? resultNode.get("accessToken").asText() : null;
                String newRefreshToken = resultNode.has("refreshToken") ? resultNode.get("refreshToken").asText()
                        : null;
                String newOpenId = resultNode.has("openId") ? resultNode.get("openId").asText() : null;
                String expiresIn = resultNode.has("expiresIn") ? resultNode.get("expiresIn").asText() : null;

                if (newAccessToken != null && newRefreshToken != null) {

                    aqaraConfig.setAccessToken(newAccessToken);
                    aqaraConfig.setRefreshToken(newRefreshToken);
                    aqaraConfig.setOpendId(newOpenId);
                    aqaraConfig.setExpiresIn(expiresIn);
                    // cập nhập dữ liệu lại vào trong database
                    AqaraConfig updateAqaraConfig = aqaraConfigRepository.save(aqaraConfig);
                    AqaraConfigMapper.mapToAqaraConfigDto(updateAqaraConfig);
                    System.out.println(" Access Token Updated: " + newAccessToken);
                    System.out.println(" Refresh Token Updated: " + newRefreshToken);
                    System.out.println(" Open Id Updated: " + newOpenId);
                    System.out.println(" Expires In Updated: " + expiresIn);
                } else {
                    System.err.println("Missing accessToken or refreshToken in response.");
                }
            } else {
                System.err.println("'result' field is missing in the API response.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("compare-temperature")
    public ResponseEntity<String> compareTemperature() {
        JSONObject json = aqaraService.compareTemperature();
        return ResponseEntity.ok().body(json.toString());
    }
}
