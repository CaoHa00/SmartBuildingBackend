package com.example.SmartBuildingBackend.service.provider.tuya;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentDto;
import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;
import com.example.SmartBuildingBackend.entity.equipment.Value;

public interface TuyaService {
  String getAccessToken(String url, String method, String body);

  String getElevatorElectric(List<Equipment> electricElevators, List<EquipmentState> electricElevatorStates,
      Long timeStamp,
      List<Value> values);

  // String extractPropertiesFromResponse(String responseBody, EquipmentDto
  // equipment);

  JSONObject parsePhaseA(String base64Value);

  ResponseEntity<String> getResponse(String url, String method, String body);

  String getListDevicesProperty();

  String createTuyaSpace(String spaceName, UUID spaceId);

  String getResponseSpaceIdFromBody(String responseBody);

  String createTuyaGroup(String spaceTuyaPlatformId, String groupName);

  String getStatusLight(List<Equipment> equipments, List<EquipmentState> equipmentStates, Long timeStamp, Value value);

  String controlLight(UUID spaceId, int valueLight);

}
