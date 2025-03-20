package com.example.SmartBuildingBackend.service.implementation;

import java.util.HashMap;
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
            aqaraConfig.getAppKey()
        );

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
        ResponseEntity<String> response = restTemplate.exchange(aqaraConfig.getUrl(), HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    
    
    
}
