package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.LogAqaraDto;

public interface LogAqaraService {

    LogAqaraDto addLogAqara(int equipmentId, LogAqaraDto logAqaraDto);

    void deleteLogAqara(int logAqaraId);

    List<LogAqaraDto> getAllLogAqaras();

    LogAqaraDto getLogAqaraById(int logAqaraId);

    LogAqaraDto updateLogAqara(int logAqaraId, LogAqaraDto logAqaraDto);

}
