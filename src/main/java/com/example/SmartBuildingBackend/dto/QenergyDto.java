package com.example.SmartBuildingBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QenergyDto {
    private Long id;
    private String timestamp;
    private double cumulativeEnergy;
}
