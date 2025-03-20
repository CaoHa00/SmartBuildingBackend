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
import com.example.SmartBuildingBackend.service.AqaraService;
import com.example.SmartBuildingBackend.utils.CreateSign;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AqaraServiceImplemetation implements AqaraService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AqaraConfig aqaraConfig;

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
    public String queryTemparatureAttributes()
            throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("intent", "query.resource.value");

        // Create a single resource entry
        Map<String, Object> resource = new HashMap<>();
        resource.put("subjectId", "lumi.54ef441000a4894c");

        // Add resourceIds as a List
        List<String> resourceIds = new ArrayList<>();
        resourceIds.add("0.1.85");
        resourceIds.add("0.2.85");
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
}
