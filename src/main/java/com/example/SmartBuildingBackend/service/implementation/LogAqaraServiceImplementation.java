package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.LogAqaraDto;
import com.example.SmartBuildingBackend.entity.Block;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.LogAqara;
import com.example.SmartBuildingBackend.entity.Room;
import com.example.SmartBuildingBackend.mapper.LogAqaraMapper;
import com.example.SmartBuildingBackend.repository.BlockRepository;
import com.example.SmartBuildingBackend.repository.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.LogAqaraRepository;
import com.example.SmartBuildingBackend.service.LogAqaraService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LogAqaraServiceImplementation implements LogAqaraService {
    private LogAqaraRepository logAqaraRepository;
    private EquipmentRepository equipmentRepository;

    @Override
    public LogAqaraDto addLogAqara(Long equipmentId, LogAqaraDto logAqaraDto) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Room is not found:" + equipmentId));

        LogAqara logAqara = LogAqaraMapper.mapToLogAqara(logAqaraDto);
        logAqara.setEquipment(equipment);
        LogAqara saveLogAqara = logAqaraRepository.save(logAqara);
        return LogAqaraMapper.mapToLogAqaraDto(saveLogAqara);

    }

    @Override
    public void deleteLogAqara(Long logAqaraId) {
        LogAqara logAqara = logAqaraRepository.findById(logAqaraId)
                .orElseThrow(() -> new RuntimeException("LogAqara is not found:" + logAqaraId));
        logAqaraRepository.delete(logAqara);
    }

    @Override
    public List<LogAqaraDto> getAllLogAqaras() {
        List<LogAqara> logAqaras = logAqaraRepository.findAll();
        return logAqaras.stream()
                .sorted((log1, log2) -> Long.compare(log2.getLogId(), log1.getLogId())) // Sort by ID in descending
                                                                                        // order
                .map(LogAqaraMapper::mapToLogAqaraDto)
                .toList();
    }

    @Override
    public LogAqaraDto getLogAqaraById(Long logAqaraId) {
        LogAqara logAqara = logAqaraRepository.findById(logAqaraId)
                .orElseThrow(() -> new RuntimeException("LogAqara is not found:" + logAqaraId));
        return LogAqaraMapper.mapToLogAqaraDto(logAqara);
    }

    @Override
    public LogAqaraDto updateLogAqara(Long logAqaraId, LogAqaraDto logAqaraDto) {
        LogAqara logAqara = logAqaraRepository.findById(logAqaraId)
                .orElseThrow(() -> new RuntimeException("LogAqara is not found:" + logAqaraId));
        logAqara.setTime(logAqaraDto.getTime());
        logAqara.setCostEnergy(logAqaraDto.getCostEnergy());
        logAqara.setLoadPower(logAqaraDto.getLoadPower());
        logAqara.setLux(logAqaraDto.getLux());
        logAqara.setTemperature(logAqaraDto.getTemperature());
        logAqara.setHumidity(logAqaraDto.getHumidity());
        logAqara.setPressure(logAqaraDto.getPressure());
        logAqara.setEquipment(logAqaraDto.getEquipment());
        LogAqara updatedLogAqara = logAqaraRepository.save(logAqara);
        return LogAqaraMapper.mapToLogAqaraDto(updatedLogAqara);
    }

}
