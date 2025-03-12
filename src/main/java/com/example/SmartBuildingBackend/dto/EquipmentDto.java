package com.example.SmartBuildingBackend.dto;


import java.util.List;

import com.example.SmartBuildingBackend.entity.LogAqara;
import com.example.SmartBuildingBackend.entity.LogUHoo;
import com.example.SmartBuildingBackend.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDto {
    private Integer equipmentId;
    private String equipmentName;
    private String equipmentType;
    private Room room;
    private List<LogUHoo> logUHoos;
    private List<LogAqara> logAqaras;
}
