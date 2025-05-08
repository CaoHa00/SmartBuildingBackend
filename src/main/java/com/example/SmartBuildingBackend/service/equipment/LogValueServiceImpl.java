package com.example.SmartBuildingBackend.service.equipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.LogValue;
import com.example.SmartBuildingBackend.mapper.equipment.LogValueMapper;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.equipment.LogValueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LogValueServiceImpl implements LogValueService {

    private final LogValueRepository logValueRepository;
    private final EquipmentRepository equipmentRepository;

    @Override
    public LogValueDto addLogValue(LogValueDto logValueDto) { // only be added from Equipment State
        LogValue logValue = LogValueMapper.mapToLogValue(logValueDto);
        LogValue savedLogValue = logValueRepository.save(logValue);
         return LogValueMapper.mapToLogValueDto(savedLogValue);
    }

    @Override
    public void deleteLogValue(UUID logValueId) {
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
    public LogValueDto getLogValueById(UUID logValueId) {
        LogValue logValue = logValueRepository.findById(logValueId)
                .orElseThrow(() -> new RuntimeException("LogValue not found with id: " + logValueId));
        return LogValueMapper.mapToLogValueDto(logValue);
    }

    @Override
    public LogValueDto updateLogValue(UUID logValueId, LogValueDto logValueDto) {
        LogValue logValue = logValueRepository.findById(logValueId)
                .orElseThrow(() -> new RuntimeException("LogValue not found with id: " + logValueId));

        logValue.setTimeStamp(logValueDto.getTimeStamp());

        LogValue updatedLogValue = logValueRepository.save(logValue);
        return LogValueMapper.mapToLogValueDto(updatedLogValue);
    }

    @Override
    public List<LogValue> getLatestStatusList(UUID equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));

        List<LogValue> logValues = equipment.getLogValues();
        if (logValues == null || logValues.isEmpty()) {
            return Collections.emptyList();
        }

        // Map of valueName → latest LogValue (by timestamp)
        Map<String, LogValue> latestByValueName = new HashMap<>();

        for (LogValue log : logValues) {
            String valueName = log.getValue().getValueName();
            LogValue existing = latestByValueName.get(valueName);

            if (existing == null || log.getTimeStamp() > existing.getTimeStamp()) {
                latestByValueName.put(valueName, log); // keep the newest one
            }
        }
        return new ArrayList<>(latestByValueName.values());
    }
    @Override
    public List<LogValue> saveAll(List<LogValue> LogValues) {
        return logValueRepository.saveAll(LogValues);
    }
}
