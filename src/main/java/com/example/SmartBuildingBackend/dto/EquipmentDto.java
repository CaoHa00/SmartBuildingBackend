package com.example.SmartBuildingBackend.dto;


import java.util.List;

import com.example.SmartBuildingBackend.entity.Category;
import com.example.SmartBuildingBackend.entity.EquipmentType;
import com.example.SmartBuildingBackend.entity.LogValue;
import com.example.SmartBuildingBackend.entity.Room;

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
    private Long equipmentId;

    @NotBlank(message = "Equipment name is required")
    private String equipmentName;

    @NotBlank(message = "Device id is required")
    private String deviceId;

    @NotBlank(message = "Equipment type is required")
    private Long equipmentTypeId;
    
    private Long roomId;
    private List<LogValue> logValues;
    private Long categoryId;
}
