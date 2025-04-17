package com.example.SmartBuildingBackend.service;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.entity.LogValue;

public interface LogValueService {
    LogValueDto addLogValue(UUID equipmentId, UUID valueId, LogValueDto logValueDto);
    
    void deleteLogValue(UUID logValueId);

    List<LogValueDto> getAllLogValues();

    LogValueDto getLogValueById(UUID logValueId);

    LogValueDto updateLogValue(UUID logValueId, LogValueDto logValueDto);

    List<LogValue> getLatestStatusList(UUID equipmentId);
    
    boolean existsByTimestampAndValueIdAndEquipmentId(Long timeStamp, UUID valueId, UUID equipmentId);

}
