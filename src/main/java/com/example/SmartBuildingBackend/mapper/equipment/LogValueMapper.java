package com.example.SmartBuildingBackend.mapper.equipment;

import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.LogValue;
import com.example.SmartBuildingBackend.entity.equipment.Value;

public class LogValueMapper {
    public static LogValueDto mapToLogValueDto(LogValue logValue) {
        return new LogValueDto(
                logValue.getLogValueId(),
                logValue.getTimeStamp(),
                logValue.getEquipment() != null ? logValue.getEquipment().getEquipmentId() : null,
                logValue.getValue() != null ? logValue.getValue().getValueId() : null,
                logValue.getValueResponse());
    }

    public static LogValue mapToLogValue(LogValueDto logValueDto) {
        LogValue logValue = new LogValue();
        if (logValueDto.getEquipmentId() != null) {
            Equipment equipment = new Equipment();
            equipment.setEquipmentId(logValueDto.getEquipmentId());
            logValue.setEquipment(equipment);
        }
        if (logValueDto.getValueId() != null) {
            Value value = new Value();
            value.setValueId(logValueDto.getValueId());
            logValue.setValue(value);
        }
        logValue.setLogValueId(logValueDto.getLogValueId());
        logValue.setTimeStamp(logValueDto.getTimeStamp());
        logValue.setValueResponse(logValueDto.getValueResponse());
        return logValue;
    }
}
