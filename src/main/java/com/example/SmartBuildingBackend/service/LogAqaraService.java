package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.LogAqaraDto;

public interface LogAqaraService {

    LogAqaraDto addLogAqara(Long equipmentId, LogAqaraDto logAqaraDto);

    void deleteLogAqara(Long logAqaraId);

    List<LogAqaraDto> getAllLogAqaras();

    LogAqaraDto getLogAqaraById(Long logAqaraId);

    LogAqaraDto updateLogAqara(Long logAqaraId, LogAqaraDto logAqaraDto);

}
