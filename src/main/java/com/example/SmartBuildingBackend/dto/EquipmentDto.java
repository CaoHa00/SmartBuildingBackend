package com.example.SmartBuildingBackend.dto;


import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.entity.LogValue;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDto {
    private UUID equipmentId;

    @NotBlank(message = "Equipment name is required")
    private String equipmentName;

    @NotBlank(message = "Device id is required")
    private String deviceId;

    @NotBlank(message = "Equipment type is required")
    private UUID equipmentTypeId;
    
    private UUID spaceId;
    private List<LogValue> logValues;
    private UUID categoryId;
}
