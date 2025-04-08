package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.entity.LogValue;

public class LogValueMapper {
    public static LogValueDto mapToLogValueDto(LogValue logValue) {
        return new LogValueDto(
                logValue.getLogValueId(),
                logValue.getTimeStamp(),
                logValue.getEquipment() != null ? logValue.getEquipment().getEquipmentId() : null,
                logValue.getValue(),
                logValue.getValueResponse()
        );
    }

    public static LogValue mapToLogValue(LogValueDto logValueDto) {
        LogValue  logValue = new LogValue();
        logValue.setLogValueId(logValueDto.getLogValueId());
        logValue.setTimeStamp(logValueDto.getTimeStamp());
        logValue.setValue(logValueDto.getValue());
        logValue.setValueResponse(logValueDto.getValueResponse());
        return logValue;
    }
}
