package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.entity.Equipment;

public class EquipmentMapper {
    public static EquipmentDto mapToEquipmentDto(Equipment equipment) {
        return new EquipmentDto(
                equipment.getEquipmentId(),
                equipment.getEquipmentName(),
                equipment.getEquipmentType(),
                equipment.getRoom(),
                equipment.getLogUHoos(),
                equipment.getLogAqaras()
                );
        }
    
    public static Equipment mapToEquipment (EquipmentDto equipmentDto) {
        return new Equipment(
                equipmentDto.getEquipmentId(),
                equipmentDto.getEquipmentName(),
                equipmentDto.getEquipmentType(),
                equipmentDto.getRoom(),
                equipmentDto.getLogUHoos(),
                equipmentDto.getLogAqaras()
                );
        }
}
