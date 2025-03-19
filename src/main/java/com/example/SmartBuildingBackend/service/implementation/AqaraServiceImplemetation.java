package com.example.SmartBuildingBackend.service.implementation;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.SmartBuildingBackend.service.AqaraService;
import com.example.SmartBuildingBackend.utils.CreateSign;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class AqaraServiceImplemetation implements AqaraService {
    
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String url = "https://open-sg.aqara.com/v3.0/open/api";
    private static final String accessToken = "9b4d45152ac0febc53c66f1d7b18c9b2";
    private static final String appId = "1351160374798168064cbbe0";
    private static final String keyId = "K.1351160375179849728";
    private static final String appKey = "nwqnes2sg1m1wmud6snlmsxwh1v9gkjc";
    
    @Override
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
