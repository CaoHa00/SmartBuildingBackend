package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.EquipmentDto;

public interface EquipmentService {
    List<EquipmentDto> getAllEquipments();
    EquipmentDto getEquipmentById(int equipmentId);
    EquipmentDto updateEquipment(int equipmentId, EquipmentDto updateEquipment);
    void deleteEquipment(int equipmentId);
    EquipmentDto addEquipment(int equipmentId,EquipmentDto equipmentDto);
}
