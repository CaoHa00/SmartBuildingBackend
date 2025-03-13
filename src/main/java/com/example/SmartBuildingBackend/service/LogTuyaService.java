package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.LogTuyaDto;

public interface LogTuyaService {

    LogTuyaDto addLogTuya(Long equipmentId, LogTuyaDto logTuyaDto);

    void deleteLogTuya(Long logTuyaId);

    List<LogTuyaDto> getAllLogTuyas();

    LogTuyaDto getLogTuyaById(Long logTuyaId);

    LogTuyaDto updateLogTuya(Long logTuyaId, LogTuyaDto logTuyaDto);
}
