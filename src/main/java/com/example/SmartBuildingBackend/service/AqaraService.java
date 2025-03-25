package com.example.SmartBuildingBackend.service;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

@Service
public interface AqaraService {
    String sendRequestToAqara(Map<String, Object> requestBody) throws Exception;

    String queryDetailsAttributes(Map<String, Object> requestBody, String model, String resourceId) throws Exception;

    // ResourceId is set in site the function 0.1.85 - temperature
    String queryTemparatureAttributes(String deviceId) throws Exception; // input DeviceId to get it temperature "example lumi.54ef441000a4894c"

    String convertToJson(Map<String, Object> request);

    ObjectNode getJsonAPIFromServer(String response,EquipmentDto equipmentDto, String nameValue);
}
