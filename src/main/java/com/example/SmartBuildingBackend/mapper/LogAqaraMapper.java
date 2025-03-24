package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.LogAqaraDto;
import com.example.SmartBuildingBackend.entity.LogAqara;
public class LogAqaraMapper {
    public static LogAqaraDto mapToLogAqaraDto(LogAqara logAqara) {
        return new LogAqaraDto(
                logAqara.getLogId(),
                logAqara.getTime(),
                logAqara.getCostEnergy(),
                logAqara.getLoadPower(),
                logAqara.getLux(),
                logAqara.getTemperature(),
                logAqara.getHumidity(),
                logAqara.getPressure(),
                logAqara.getEquipment());
    }

    public static LogAqara mapToLogAqara(LogAqaraDto logAqaraDto) {
        return new LogAqara(
                logAqaraDto.getLogAqaraId(),
                logAqaraDto.getTime(),
                logAqaraDto.getCostEnergy(),
                logAqaraDto.getLoadPower(),
                logAqaraDto.getLux(),
                logAqaraDto.getTemperature(),
                logAqaraDto.getHumidity(),
                logAqaraDto.getPressure(),
                logAqaraDto.getEquipment());
    }
}
