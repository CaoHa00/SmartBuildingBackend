package com.example.SmartBuildingBackend.dto.provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QEnergyDto {
    private Long id;
    private String timestamp;
    private double cumulativeEnergy;
}
