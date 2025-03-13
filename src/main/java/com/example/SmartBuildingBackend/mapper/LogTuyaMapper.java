package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.LogTuyaDto;
import com.example.SmartBuildingBackend.entity.LogTuya;

public class LogTuyaMapper {
public static LogTuyaDto mapToLogTuyaDto(LogTuya logTuya) {
    return new LogTuyaDto(
        logTuya.getLogId(),
        logTuya.getTime(),
        logTuya.getElectricalEnergy(),
        logTuya.getEquipment()
    );
}

public static LogTuya mapToLogTuya(LogTuyaDto logTuyaDto) {
    return new LogTuya(
        logTuyaDto.getLogId(),
        logTuyaDto.getTime(),
        logTuyaDto.getElectricalEnergy(),
        logTuyaDto.getEquipment()
    );
}

}
