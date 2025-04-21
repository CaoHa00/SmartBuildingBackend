package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.QenergyDto;
import com.example.SmartBuildingBackend.entity.QEnegery;

public class QengeryMapper {
    public static QenergyDto mapToQenergyDto(QEnegery qEnegery) {
        return new QenergyDto(
                qEnegery.getId(),
                qEnegery.getTimestamp().toString(),
                qEnegery.getCumulativeEnergy()
        );
    }
    public static QEnegery mapToQenergy(QenergyDto qenergyDto) {
        return new QEnegery(
                qenergyDto.getId(),
                java.time.LocalDateTime.parse(qenergyDto.getTimestamp()),
                qenergyDto.getCumulativeEnergy()
        );
    }
}
