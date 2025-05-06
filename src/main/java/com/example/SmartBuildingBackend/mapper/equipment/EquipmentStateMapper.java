package com.example.SmartBuildingBackend.mapper.equipment;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;
import com.example.SmartBuildingBackend.entity.equipment.Value;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.equipment.ValueRepository;

public class EquipmentStateMapper {

    private final EquipmentRepository equipmentRepository;
    private final ValueRepository valueRepository;

    // Constructor to inject repositories
    public EquipmentStateMapper(EquipmentRepository equipmentRepository, ValueRepository valueRepository) {
        this.equipmentRepository = equipmentRepository;
        this.valueRepository = valueRepository;
    }

    // Convert from EquipmentState entity to DTO
    public static EquipmentStateDto mapToDto(EquipmentState equipmentState) {
        if (equipmentState == null) {
            return null;
        }

        EquipmentStateDto dto = new EquipmentStateDto();
        dto.setEquipmentStateId(equipmentState.getEquipmentStateId());
        dto.setTimeStamp(equipmentState.getTimeStamp());
        dto.setEquipmentId(
                equipmentState.getEquipment() != null ? equipmentState.getEquipment().getEquipmentId() : null);
        dto.setValueId(equipmentState.getValue() != null ? equipmentState.getValue().getValueId() : null); // Added null
                                                                                                           // check
        dto.setValueResponse(equipmentState.getValueResponse());
        return dto;
    }

    // Convert from DTO to EquipmentState entity
    public static EquipmentState mapToEntity(EquipmentStateDto equipmentStateDto) {
        if (equipmentStateDto == null) {
            return null;
        }

        EquipmentState entity = new EquipmentState();
        entity.setEquipmentStateId(equipmentStateDto.getEquipmentStateId());
        entity.setTimeStamp(equipmentStateDto.getTimeStamp());

        // Check if equipmentId is provided, create Equipment object with ID
        if (equipmentStateDto.getEquipmentId() != null) {
            Equipment equipment = new Equipment();
            equipment.setEquipmentId(equipmentStateDto.getEquipmentId());
            entity.setEquipment(equipment); // Set Equipment with just ID
        }

        // Check if valueId is provided, create Value object with ID
        if (equipmentStateDto.getValueId() != null) {
            Value value = new Value();
            value.setValueId(equipmentStateDto.getValueId());
            entity.setValue(value); // Set Value with just ID
        }
        entity.setValueResponse(equipmentStateDto.getValueResponse());

        return entity;
    }
}
