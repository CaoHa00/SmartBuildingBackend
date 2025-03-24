package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.EquipmentDto;

public interface EquipmentService {
    List<EquipmentDto> getAllEquipments();
    EquipmentDto getEquipmentById(Long equipmentId);
    EquipmentDto updateEquipment(Long equipmentId, EquipmentDto updateEquipment);
    void deleteEquipment(Long equipmentId);
    EquipmentDto addEquipment(Long roomId, Long equipmentTypeId, EquipmentDto equipmentDto);
}
