package com.example.SmartBuildingBackend.service.equipment;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.entity.equipment.LogValue;

public interface LogValueService {
    LogValueDto addLogValue(LogValueDto logValueDto);
    
    void deleteLogValue(UUID logValueId);

    List<LogValueDto> getAllLogValues();

    LogValueDto getLogValueById(UUID logValueId);

    LogValueDto updateLogValue(UUID logValueId, LogValueDto logValueDto);

    List<LogValue> getLatestStatusList(UUID equipmentId);
    
   List<LogValue> saveAll(List<LogValue> LogValues);


}
