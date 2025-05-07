package com.example.SmartBuildingBackend.service.equipment;

import java.util.List;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;

public interface EquipmentStateService {
    
    // EquipmentStateDto saveOrUpdateEquipmentState(EquipmentStateDto equipmentStateDto, String valueName);

    List<EquipmentStateDto> getAllEquipmentStates();

    List<EquipmentState> saveAll(List<EquipmentState> equipmentStates);
    
    List<EquipmentState> getCachedEquipmentStates();
}


