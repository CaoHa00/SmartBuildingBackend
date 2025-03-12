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
public class LogAqaraDto {
    private long logAqaraId;
    private long time;
    private double costEnergy;
    private double loadPower;
    private double lux;
    private double temperature;
    private double humidity;
    private double pressure;
    private Equipment equipment;
}
