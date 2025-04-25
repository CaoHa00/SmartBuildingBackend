package com.example.SmartBuildingBackend.service.equipment;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentTypeDto;

public interface EquipmentTypeService {
    EquipmentTypeDto addEquipmentType(EquipmentTypeDto equipmentTypeDto);

    EquipmentTypeDto updateEquipmentTypeDto(EquipmentTypeDto equipmentTypeDto, UUID Id);

    EquipmentTypeDto getEquipmentTypeById(UUID Id);

    void deleteEquipementType(UUID Id);

    List<EquipmentTypeDto> getAllEquipmentType();
}
