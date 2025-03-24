package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.LogValueDto;

public interface LogValueService {
    LogValueDto addLogValue(Long equipmentId, Long valueId, LogValueDto logValueDto);
    
    void deleteLogValue(Long logValueId);

    List<LogValueDto> getAllLogValues();

    LogValueDto getLogValueById(Long logValueId);

    LogValueDto updateLogValue(Long logValueId, LogValueDto logValueDto);
}
