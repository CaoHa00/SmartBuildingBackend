package com.example.SmartBuildingBackend.service.equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;
import com.example.SmartBuildingBackend.dto.equipment.LogValueDto;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;
import com.example.SmartBuildingBackend.entity.equipment.LogValue;
import com.example.SmartBuildingBackend.entity.equipment.Value;
import com.example.SmartBuildingBackend.mapper.equipment.EquipmentStateMapper;
import com.example.SmartBuildingBackend.mapper.equipment.LogValueMapper;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentStateRepository;
import com.example.SmartBuildingBackend.repository.equipment.LogValueRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentStateImplementation implements EquipmentStateService {

    private final EquipmentStateRepository equipmentStateRepository;
    private final LogValueRepository logValueRepository;
    private final ValueService valueService;
    private final AtomicReference<List<EquipmentState>> cachedEquipmentStates = new AtomicReference<>(new ArrayList<>());

    @PostConstruct
    private void init() {
        updateCache(); // get all equipment data when the service starts
    }

    // public EquipmentStateDto saveOrUpdateEquipmentState(EquipmentStateDto equipmentStateDto, String valueName) {

    //     if (valueName == null) {
    //         return equipmentStateDto;
    //     }
    //     LogValueDto logValue = new LogValueDto();
    //     logValue.setEquipmentId(equipmentStateDto.getEquipmentId());

    //     if (equipmentStateDto.getValueId() == null) {
    //         Value value = valueService.getValueByName(valueName);
    //         equipmentStateDto.setValueId(value.getValueId());
    //         logValue.setValueId(equipmentStateDto.getValueId());
    //     }
    //     Optional<EquipmentState> existingStateOptional = equipmentStateRepository
    //             .findByEquipmentEquipmentIdAndValueValueId(
    //                     equipmentStateDto.getEquipmentId(),
    //                     equipmentStateDto.getValueId());
    //     long timeStamp = System.currentTimeMillis();

    //     if (existingStateOptional.isPresent()) {
    //         // Update existing state
    //         EquipmentStateDto equipmentStateChange = EquipmentStateMapper.mapToDto(existingStateOptional.get());
    //         equipmentStateChange.setValueResponse(equipmentStateDto.getValueResponse());

    //         if (timeStamp - equipmentStateChange.getTimeStamp() >= 300000 || valueName.equals("light-switch")
    //                 || Math.abs(equipmentStateDto.getValueResponse() - equipmentStateChange.getValueResponse()) > 0.5) {
    //             logValue.setValueResponse(equipmentStateChange.getValueResponse()); // log old value
    //             // Save log
    //             equipmentStateRepository.save(EquipmentStateMapper.mapToEntity(equipmentStateChange)); // save
    //             logValue.setTimeStamp(timeStamp);
    //             LogValue logValueEntity = LogValueMapper.mapToLogValue(logValue);
    //             logValueRepository.save(logValueEntity);
    //             System.out.println("log value saved");
    //         } // each 5 mins save once
    //     } else {
    //         equipmentStateRepository.save(EquipmentStateMapper.mapToEntity(equipmentStateDto)); // create
    //     }
    //     return equipmentStateDto;
    // }

    @Override
    public List<EquipmentStateDto> getAllEquipmentStates() {
        List<EquipmentState> states = equipmentStateRepository.findAll();
        return states.stream()
                .map(EquipmentStateMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipmentState> saveAll(List<EquipmentState> equipmentStates) {
        updateCache();
        return equipmentStateRepository.saveAll(equipmentStates);
    }

    private void updateCache() {
        List<EquipmentState> updatedEquipmentStates = equipmentStateRepository.findAll();
        cachedEquipmentStates.set(updatedEquipmentStates); // Set the updated list to cache
    }

    // Get the cached equipment
    @Override
    public List<EquipmentState> getCachedEquipmentStates() {
        return cachedEquipmentStates.get(); // Thread-safe read of the cache
    }
}