package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.entity.Category;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.EquipmentType;
import com.example.SmartBuildingBackend.entity.campus.Space;


public class EquipmentMapper {

    public static  EquipmentDto mapToEquipmentDto(Equipment equipment) {
        if (equipment == null) {
            return null;
        }

        EquipmentDto dto = new EquipmentDto();
        dto.setEquipmentId(equipment.getEquipmentId());
        dto.setEquipmentName(equipment.getEquipmentName());
        dto.setDeviceId(equipment.getDeviceId());
        dto.setEquipmentTypeId(equipment.getEquipmentType().getEquipmentTypeId());
        dto.setSpaceId(equipment.getSpace().getSpaceId());
        dto.setCategoryId(equipment.getCategory().getCategoryId());
        dto.setLogValues(equipment.getLogValues());

        return dto;
    }

    public static Equipment mapToEquipment(EquipmentDto dto, Category category, EquipmentType type, Space space) {
        if (dto == null) return null;
    
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(dto.getEquipmentId());
        equipment.setEquipmentName(dto.getEquipmentName());
        equipment.setDeviceId(dto.getDeviceId());
        equipment.setLogValues(dto.getLogValues());
    
        equipment.setCategory(category);
        equipment.setEquipmentType(type);
        equipment.setSpace(space);
    
        return equipment;
    }
    
}