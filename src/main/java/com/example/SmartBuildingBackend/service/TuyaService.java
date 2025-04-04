package com.example.SmartBuildingBackend.service;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.LogValueDto;

public interface TuyaService {
    String getAccessToken();

    void fetchAccessToken();

    String getDeviceProperty(Long equipmentId);

    String extractPropertiesFromResponse(String responseBody, EquipmentDto equipment);

    JSONObject parsePhaseA(String base64Value);

    ResponseEntity<String> getResponse(String url, String method, String body);

    LogValueDto addLogValue();

    String getListDevicesProperty();
}
