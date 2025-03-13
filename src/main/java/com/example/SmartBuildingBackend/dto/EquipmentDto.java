package com.example.SmartBuildingBackend.dto;


import java.util.List;

import com.example.SmartBuildingBackend.entity.LogAqara;
import com.example.SmartBuildingBackend.entity.LogUHoo;
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

    @NotBlank(message = "Equipment type is required")
    private String equipmentType;
    private Room room;
    private List<LogUHoo> logUHoos;
    private List<LogAqara> logAqaras;
}
