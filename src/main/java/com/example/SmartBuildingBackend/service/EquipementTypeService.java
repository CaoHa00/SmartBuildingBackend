package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.EquipmentTypeDto;

public interface EquipementTypeService {
    EquipmentTypeDto addEquipmentType(EquipmentTypeDto equipmentTypeDto);

    EquipmentTypeDto updateEquipmentTypeDto(EquipmentTypeDto equipmentTypeDto, Long Id);

    EquipmentTypeDto getEquipmentTypeById(Long Id);

    void deleteEquipementType(Long Id);

    List<EquipmentTypeDto> getAllEquipmentType();
}
