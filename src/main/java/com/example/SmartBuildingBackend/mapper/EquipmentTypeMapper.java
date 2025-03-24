package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.EquipmentTypeDto;
import com.example.SmartBuildingBackend.entity.EquipmentType;

public class EquipmentTypeMapper {
    public static EquipmentTypeDto mapToEquipmentTypeDto(EquipmentType equipmentType) {
        return new EquipmentTypeDto(
                equipmentType.getEquipmentTypeId(),
                equipmentType.getEquipmentTypeName(),
                equipmentType.getEquipments());
    }

    public static EquipmentType mapToEquipmentType(EquipmentTypeDto equipmentTypeDto) {
        return new EquipmentType(
                equipmentTypeDto.getEquipmentId(),
                equipmentTypeDto.getEquipmentTypeName(),
                equipmentTypeDto.getEquipments());
    }
}
