package com.example.SmartBuildingBackend.service;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.EquipmentDto;

public interface EquipmentService {
    List<EquipmentDto> getAllEquipments();

    EquipmentDto getEquipmentById(UUID equipmentId);

    EquipmentDto updateEquipment(UUID equipmentId, EquipmentDto updateEquipment);

    void deleteEquipment(UUID equipmentId);

    EquipmentDto addEquipment(UUID spaceId, UUID equipmentTypeId,UUID categoryId, EquipmentDto equipmentDto);

    
}
