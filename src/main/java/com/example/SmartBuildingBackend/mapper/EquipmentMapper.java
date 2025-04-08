package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.entity.Equipment;

public class EquipmentMapper {
        public static EquipmentDto mapToEquipmentDto(Equipment equipment) {
                return new EquipmentDto(
                                equipment.getEquipmentId(),
                                equipment.getEquipmentName(),
                                equipment.getDeviceId(),
                                equipment.getEquipmentType() != null ? equipment.getEquipmentType().getEquipmentTypeId()
                                                : null,
                                equipment.getRoom() != null ? equipment.getRoom().getRoomId() : null,
                                equipment.getLogValues(),
                                equipment.getCategory() != null ? equipment.getCategory().getCategoryId() : null);
        }

        public static Equipment mapToEquipment(EquipmentDto equipmentDto) {
                Equipment equipment = new Equipment();
                equipment.setEquipmentId(equipmentDto.getEquipmentId());
                equipment.setEquipmentName(equipmentDto.getEquipmentName());
                equipment.setDeviceId(equipmentDto.getDeviceId());
                // equipmentType, room, and category should be set later in the service using
                // repositories
                return equipment;
        }
}
