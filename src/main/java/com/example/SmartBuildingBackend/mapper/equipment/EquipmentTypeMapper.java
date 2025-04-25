package com.example.SmartBuildingBackend.mapper.equipment;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentTypeDto;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentType;

public class EquipmentTypeMapper {
    public static EquipmentTypeDto mapToEquipmentTypeDto(EquipmentType equipmentType) {
        return new EquipmentTypeDto(
                equipmentType.getEquipmentTypeId(),
                equipmentType.getEquipmentTypeName(),
                equipmentType.getEquipments());
    }

    public static EquipmentType mapToEquipmentType(EquipmentTypeDto equipmentTypeDto) {
        return new EquipmentType(
                equipmentTypeDto.getEquipmentTypeId(),
                equipmentTypeDto.getEquipmentTypeName(),
                equipmentTypeDto.getEquipments());
    }
}
