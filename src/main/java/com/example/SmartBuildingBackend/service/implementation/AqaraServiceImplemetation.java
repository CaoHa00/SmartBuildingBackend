package com.example.SmartBuildingBackend.service.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SmartBuildingBackend.configuration.AqaraConfig;
import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.service.AqaraService;
import com.example.SmartBuildingBackend.service.LogValueService;
import com.example.SmartBuildingBackend.service.ValueService;
import com.example.SmartBuildingBackend.utils.CreateSign;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AqaraServiceImplemetation implements AqaraService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AqaraConfig aqaraConfig;
    private LogValueService logValueService;
    private final ValueService valueService;

    @Override
    public String sendRequestToAqara(Map<String, Object> requestBody) throws Exception {

        return sendAqaraRequest(requestBody);

    }

    @Override
    public String queryDetailsAttributes(Map<String, Object> requestBody, String model, String resourceId)
            throws Exception {
        requestBody.put("intent", "query.resource.info");
        Map<String, Object> data = new HashMap<>();
        data.put("model", model);

        if (resourceId != null) {
            data.put("resourceId", resourceId);
        }

        requestBody.put("data", data);

        // Send the request
        return sendAqaraRequest(requestBody);
    }

    private String sendAqaraRequest(Map<String, Object> requestBody) throws Exception {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String time = String.valueOf(System.currentTimeMillis());
        // Generate Sign
        String sign = CreateSign.createSign(
                aqaraConfig.getAccessToken(),
                aqaraConfig.getAppId(),
                aqaraConfig.getKeyId(),
                nonce, time,
                aqaraConfig.getAppKey());
        // Create Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accesstoken", aqaraConfig.getAccessToken().trim());
        headers.set("Appid", aqaraConfig.getAppId().trim());
        headers.set("Keyid", aqaraConfig.getKeyId().trim());
        headers.set("Nonce", nonce.trim());
        headers.set("Time", time.trim());
        headers.set("Sign", sign);
        headers.set("Lang", "en");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        // Send Request
        ResponseEntity<String> response = restTemplate.exchange(aqaraConfig.getUrl(), HttpMethod.POST, entity,
                String.class);
        return response.getBody();
    }

    @Override
    public String queryTemparatureAttributes(String deviceId)
            throws Exception {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("intent", "query.resource.value");

        // Create a single resource entry
        Map<String, Object> resource = new HashMap<>();
        resource.put("subjectId", deviceId);

        // Add resourceIds as a List
        List<String> resourceIds = new ArrayList<>();
        resourceIds.add("0.1.85");
        resource.put("resourceIds", resourceIds);

        // Wrap resource in a List
        List<Map<String, Object>> resources = Collections.singletonList(resource);

        // Create data object and add resources list
        Map<String, Object> data = new HashMap<>();
        data.put("resources", resources);

        // Add data to requestBody
        requestBody.put("data", data);

        // Send the request
        return sendAqaraRequest(requestBody);
    }

    @Override
    public String convertToJson(Map<String, Object> request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
        } catch (Exception e) {
            return "Error converting to JSON: " + e.getMessage();
        }
    }

    // method to process API response from CHINA
    @Override
    public ObjectNode getJsonAPIFromServer(String response, EquipmentDto equipmentDto, String nameValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            ArrayNode resultArray = (ArrayNode) rootNode.get("result");

            LogValueDto logValueDto = new LogValueDto();
            for (JsonNode node : resultArray) {
                JsonNode valueNode = node.get("value");
                String resourceId = node.get("resourceId").asText();
                String timeStamp = node.get("timeStamp").asText();

                // Extract temperature value if the resourceId matches

                if (valueNode != null && resourceId.equals("0.1.85")) {
                    result.put("temperature", valueNode.asText().substring(0, 2));
                    //input LogValue to store value
                    logValueDto.setTimeStamp(node.get("timeStamp").asLong());
                    logValueDto.setValueResponse(node.get("value").asLong());
                }
                result.put("timeStamp", timeStamp);
            }
            Long valueId = valueService.getValueByName(nameValue);
            logValueService.addLogValue(equipmentDto.getEquipmentId(), valueId, logValueDto);
        } catch (Exception e) {
            throw new RuntimeException("Error processing JSON response", e);
        }
        return result;
    }
}
