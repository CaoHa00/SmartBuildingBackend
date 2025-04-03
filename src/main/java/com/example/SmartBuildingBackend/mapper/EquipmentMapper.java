package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.entity.Equipment;

public class EquipmentMapper {
        public static EquipmentDto mapToEquipmentDto(Equipment equipment) {
                return new EquipmentDto(
                        equipment.getEquipmentId(),
                        equipment.getEquipmentName(),
                        equipment.getDeviceId(),
                        equipment.getEquipmentType(),
                        equipment.getRoom(),
                        equipment.getLogValues(),
                        equipment.getCategory()
                );
        }

        public static Equipment mapToEquipment(EquipmentDto equipmentDto) {
                return new Equipment(
                                equipmentDto.getEquipmentId(),
                                equipmentDto.getEquipmentName(),
                                equipmentDto.getDeviceId(),
                                equipmentDto.getEquipmentType(),
                                equipmentDto.getRoom(),
                                equipmentDto.getLogValues(),
                                equipmentDto.getCategory()
                        );
        }
}
