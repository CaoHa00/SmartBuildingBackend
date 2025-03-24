package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.entity.LogValue;

public class LogValueMapper {
    public static LogValueDto mapToLogValueDto(LogValue logValue) {
        return new LogValueDto(
                logValue.getLogValueId(),
                logValue.getTimeStamp(),
                logValue.getEquipment(),
                logValue.getValue(),
                logValue.getValueResponse()
        );
    }

    public static LogValue mapToLogValue(LogValueDto logValueDto) {
        return new LogValue(
                logValueDto.getLogValueId(),
                logValueDto.getTimeStamp(),
                logValueDto.getEquipment(),
                logValueDto.getValue(),
                logValueDto.getValueResponse()
                
        );
    }
}
