package com.example.SmartBuildingBackend.service;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.entity.Equipment;

public interface TuyaService {
    String getAccessToken(String url, String method, String body);

    String getDeviceProperty(UUID equipmentId);

    String extractPropertiesFromResponse(String responseBody, EquipmentDto equipment);

    JSONObject parsePhaseA(String base64Value);

    ResponseEntity<String> getResponse(String url, String method, String body);

    LogValueDto addLogValue();

    String getListDevicesProperty();
    
    String createTuyaSpace(String spaceName, UUID spaceId);
    
    String getResponseSpaceIdFromBody(String responseBody);

    String createTuyaGroup(String spaceTuyaPlatformId, String groupName);

    String getLatestStatusDeviceList(List<Equipment> equipments);

}
