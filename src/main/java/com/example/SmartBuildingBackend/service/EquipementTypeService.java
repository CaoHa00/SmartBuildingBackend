package com.example.SmartBuildingBackend.service;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.EquipmentTypeDto;

public interface EquipementTypeService {
    EquipmentTypeDto addEquipmentType(EquipmentTypeDto equipmentTypeDto);

    EquipmentTypeDto updateEquipmentTypeDto(EquipmentTypeDto equipmentTypeDto, UUID Id);

    EquipmentTypeDto getEquipmentTypeById(UUID Id);

    void deleteEquipementType(UUID Id);

    List<EquipmentTypeDto> getAllEquipmentType();
}
