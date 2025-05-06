package com.example.SmartBuildingBackend.service.equipment;

import java.util.List;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;

public interface EquipmentStateService {
    
    public EquipmentStateDto saveOrUpdateEquipmentState(EquipmentStateDto equipmentStateDto, String valueName);

    List<EquipmentStateDto> getAllEquipmentStates(); // NEW METHOD
}


