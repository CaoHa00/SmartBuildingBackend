package com.example.SmartBuildingBackend.service.provider.tuya;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentDto;
import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;

public interface TuyaService {
    String getAccessToken(String url, String method, String body);

    String getDeviceProperty(List<Equipment> equipments);

  //  String extractPropertiesFromResponse(String responseBody, EquipmentDto equipment);

    JSONObject parsePhaseA(String base64Value);

    ResponseEntity<String> getResponse(String url, String method, String body);


    String getListDevicesProperty();
    
    String createTuyaSpace(String spaceName, UUID spaceId);
    
    String getResponseSpaceIdFromBody(String responseBody);

    String createTuyaGroup(String spaceTuyaPlatformId, String groupName);

    String getLatestStatusDeviceList(List<Equipment> equipments);

    String controlLight(UUID spaceId, int valueLight);

    
}
