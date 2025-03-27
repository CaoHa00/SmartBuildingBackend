package com.example.SmartBuildingBackend.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public interface AqaraService {
    String sendRequestToAqara(Map<String, Object> requestBody) throws Exception;

    String queryDetailsAttributes(Map<String, Object> requestBody, String model, String resourceId) throws Exception;

    //ResourceId is set in site the function 0.1.85
    String queryTemparatureAttributes() throws Exception;

    String convertToJson(Map<String, Object> request);

    String authorizationVerificationCode() throws Exception;

    String createVirtualAccount() throws Exception;

    String ObtainAccessToken() throws Exception;

    String refreshToken() throws Exception;
}

    

    

