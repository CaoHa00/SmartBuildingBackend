package com.example.SmartBuildingBackend.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

@Service
public interface AqaraService {
    String sendRequestToAqara(Map<String, Object> requestBody) throws Exception;

    String queryDetailsAttributes(Map<String, Object> requestBody, String model, String resourceId) throws Exception;

    String convertToJson(Map<String, Object> request);

    String authorizationVerificationCode() throws Exception;

    String createVirtualAccount() throws Exception;

    String ObtainAccessToken() throws Exception;

    String refreshToken() throws Exception;

    ObjectNode getJsonAPIFromServer(String response, Long equipmentId);

    String queryTemparatureAttributes(Long equipmentId) throws Exception;

    JSONObject compareTemperature();
     
    String queryLightControl(Long equipmentId, Long value, Long buttonPosition) throws Exception;;
}
