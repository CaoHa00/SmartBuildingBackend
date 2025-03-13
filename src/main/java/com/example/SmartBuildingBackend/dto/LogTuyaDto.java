package com.example.SmartBuildingBackend.dto;

import com.example.SmartBuildingBackend.entity.Equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogTuyaDto {
    private Long logId;
    private long time;
    private double electricalEnergy;
    private Equipment equipment;
}
