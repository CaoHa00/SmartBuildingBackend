package com.example.SmartBuildingBackend.dto;

import java.util.List;

import com.example.SmartBuildingBackend.entity.Equipment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipmentTypeDto {
    private Long equipmentId;
    @NotBlank(message = "Equipment type is required")
    private String equipmentTypeName;
    private List<Equipment> equipments;
}
