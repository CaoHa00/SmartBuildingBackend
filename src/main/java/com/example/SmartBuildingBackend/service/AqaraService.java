package com.example.SmartBuildingBackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SmartBuildingBackend.utils.CreateSign;

import java.util.Map;
import java.util.UUID;

@Service
public class AqaraService {

   private final RestTemplate restTemplate = new RestTemplate();

    @Value("${aqara.api.url}")
    private String url;

    @Value("${aqara.api.accesstoken}")
    private String accessToken;

    @Value("${aqara.api.appid}")
    private String appId;

    @Value("${aqara.api.keyid}")
    private String keyId;

    @Value("${aqara.api.appkey}")
    private String appKey;

    public String sendRequestToAqara(Map<String, Object> requestBody) throws Exception {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String time = String.valueOf(System.currentTimeMillis());

        // Generate Sign
        String sign = CreateSign.createSign(accessToken, appId, keyId, nonce, time, appKey);

        // Create Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accesstoken", accessToken.trim());
        headers.set("Appid", appId.trim());
        headers.set("Keyid", keyId.trim());
        headers.set("Nonce", nonce.trim());
        headers.set("Time", time.trim());
        headers.set("Sign", sign);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        
        // Send Request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        
        return response.getBody();
        
    }

    
}
