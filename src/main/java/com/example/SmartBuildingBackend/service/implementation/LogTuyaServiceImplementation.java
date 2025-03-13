package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.LogTuyaDto;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.LogTuya;
import com.example.SmartBuildingBackend.mapper.LogTuyaMapper;
import com.example.SmartBuildingBackend.repository.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.LogTuyaRepository;
import com.example.SmartBuildingBackend.service.LogTuyaService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LogTuyaServiceImplementation implements LogTuyaService {
    private LogTuyaRepository logTuyaRepository;
    private EquipmentRepository equipmentRepository;

    @Override
    public LogTuyaDto addLogTuya(Long equipmentId, LogTuyaDto logTuyaDto) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found: " + equipmentId));

        LogTuya logTuya = LogTuyaMapper.mapToLogTuya(logTuyaDto);
        logTuya.setEquipment(equipment);
        LogTuya savedLogTuya = logTuyaRepository.save(logTuya);
        return LogTuyaMapper.mapToLogTuyaDto(savedLogTuya);
    }

    @Override
    public void deleteLogTuya(Long logTuyaId) {
        LogTuya logTuya = logTuyaRepository.findById(logTuyaId)
                .orElseThrow(() -> new RuntimeException("LogTuya not found: " + logTuyaId));
        logTuyaRepository.delete(logTuya);
    }

    @Override
    public List<LogTuyaDto> getAllLogTuyas() {
        List<LogTuya> logTuyas = logTuyaRepository.findAll();
        return logTuyas.stream()
                .sorted((log1, log2) -> Long.compare(log2.getLogId(), log1.getLogId())) // Sort by ID in descending order
                .map(LogTuyaMapper::mapToLogTuyaDto)
                .toList();
    }

    @Override
    public LogTuyaDto getLogTuyaById(Long logTuyaId) {
        LogTuya logTuya = logTuyaRepository.findById(logTuyaId)
                .orElseThrow(() -> new RuntimeException("LogTuya not found: " + logTuyaId));
        return LogTuyaMapper.mapToLogTuyaDto(logTuya);
    }

    @Override
    public LogTuyaDto updateLogTuya(Long logTuyaId, LogTuyaDto logTuyaDto) {
        LogTuya logTuya = logTuyaRepository.findById(logTuyaId)
                .orElseThrow(() -> new RuntimeException("LogTuya not found: " + logTuyaId));
        logTuya.setTime(logTuyaDto.getTime());
        logTuya.setElectricalEnergy(logTuyaDto.getElectricalEnergy());
        logTuya.setEquipment(logTuyaDto.getEquipment());
        LogTuya updatedLogTuya = logTuyaRepository.save(logTuya);
        return LogTuyaMapper.mapToLogTuyaDto(updatedLogTuya);
    }
}
