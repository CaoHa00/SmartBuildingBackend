package com.example.SmartBuildingBackend.mapper.equipment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentDto;
import com.example.SmartBuildingBackend.entity.equipment.Category;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentType;
import com.example.SmartBuildingBackend.entity.space.Space;

public class EquipmentMapper {

    public static EquipmentDto mapToEquipmentDto(Equipment equipment) {
        if (equipment == null) {
            return null;
        }
        EquipmentDto dto = new EquipmentDto();
        dto.setEquipmentId(equipment.getEquipmentId());
        dto.setEquipmentName(equipment.getEquipmentName());
        dto.setDeviceId(equipment.getDeviceId());
        dto.setEquipmentStates(equipment.getEquipmentStates());
        // Safely map equipment type
        if (equipment.getEquipmentType() != null) {
            dto.setEquipmentTypeId(equipment.getEquipmentType().getEquipmentTypeId());
        }
        // Safely map space
        if (equipment.getSpace() != null) {
            dto.setSpaceId(equipment.getSpace().getSpaceId());
        }

        // Safely map category
        if (equipment.getCategory() != null) {
            dto.setCategoryId(equipment.getCategory().getCategoryId());
        }
        return dto;
    }

    public static Equipment mapToEquipment(EquipmentDto dto, Category category, EquipmentType type, Space space) {
        if (dto == null)
            return null;
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(dto.getEquipmentId());
        equipment.setEquipmentName(dto.getEquipmentName());
        equipment.setDeviceId(dto.getDeviceId());
        equipment.setLogValues(dto.getLogValues());
        equipment.setCategory(category);
        equipment.setEquipmentType(type);
        equipment.setSpace(space);
        equipment.setEquipmentStates(dto.getEquipmentStates());
        return equipment;
    }

    public static List<EquipmentDto> mapToEquipmentDtoList(List<Equipment> equipmentList) {
        if (equipmentList == null) {
            return Collections.emptyList();
        }
        return equipmentList.stream()
                .map(EquipmentMapper::mapToEquipmentDto)
                .collect(Collectors.toList());
    }
}