package com.example.SmartBuildingBackend.mapper.provider;

import com.example.SmartBuildingBackend.dto.provider.QEnergyDto;
import com.example.SmartBuildingBackend.entity.provider.QEnergy;

public class QEnergyMapper {
    public static QEnergyDto mapToQEnegyDto (QEnergy qEnegy){
        return new QEnergyDto(
                qEnegy.getId(),
                qEnegy.getTimestamp().toString(),
                qEnegy.getCumulativeEnergy()
        );
    }
    public static QEnergy mapToQEnegy (QEnergyDto qEnegyDto){
        return new QEnergy(
                qEnegyDto.getId(),
                java.time.LocalDateTime.parse(qEnegyDto.getTimestamp()),
                qEnegyDto.getCumulativeEnergy()
        );
    }
}