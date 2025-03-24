package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.LogValue;
import com.example.SmartBuildingBackend.entity.Value;
import com.example.SmartBuildingBackend.mapper.LogValueMapper;
import com.example.SmartBuildingBackend.repository.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.LogValueRepository;
import com.example.SmartBuildingBackend.repository.ValueRepository;
import com.example.SmartBuildingBackend.service.LogValueService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LogValueServiceImpl implements LogValueService {

    private final LogValueRepository logValueRepository;
    private final EquipmentRepository equipmentRepository;
    private final ValueRepository valueRepository;

    @Override
    public LogValueDto addLogValue(Long equipmentId, Long valueId, LogValueDto logValueDto) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));

        Value value = valueRepository.findById(valueId)
                .orElseThrow(() -> new RuntimeException("Value not found with id: " + valueId));

        LogValue logValue = LogValueMapper.mapToLogValue(logValueDto);
        logValue.setEquipment(equipment);
        logValue.setValue(value);

        LogValue savedLogValue = logValueRepository.save(logValue);
        return LogValueMapper.mapToLogValueDto(savedLogValue);
    }

    @Override
    public void deleteLogValue(Long logValueId) {
        LogValue logValue = logValueRepository.findById(logValueId)
                .orElseThrow(() -> new RuntimeException("LogValue not found with id: " + logValueId));
        logValueRepository.delete(logValue);
    }

    @Override
    public List<LogValueDto> getAllLogValues() {
        List<LogValue> logValues = logValueRepository.findAll();
        return logValues.stream()
                .map(LogValueMapper::mapToLogValueDto)
                .collect(Collectors.toList());
    }

    @Override
    public LogValueDto getLogValueById(Long logValueId) {
        LogValue logValue = logValueRepository.findById(logValueId)
                .orElseThrow(() -> new RuntimeException("LogValue not found with id: " + logValueId));
        return LogValueMapper.mapToLogValueDto(logValue);
    }

    @Override
    public LogValueDto updateLogValue(Long logValueId, LogValueDto logValueDto) {
        LogValue logValue = logValueRepository.findById(logValueId)
                .orElseThrow(() -> new RuntimeException("LogValue not found with id: " + logValueId));

        logValue.setTimeStamp(logValueDto.getTimeStamp());

        LogValue updatedLogValue = logValueRepository.save(logValue);
        return LogValueMapper.mapToLogValueDto(updatedLogValue);
    }
}
