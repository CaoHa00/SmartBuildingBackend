package com.example.SmartBuildingBackend.service.equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;
import com.example.SmartBuildingBackend.mapper.equipment.EquipmentStateMapper;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentStateRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentStateImplementation implements EquipmentStateService {

    private final EquipmentStateRepository equipmentStateRepository;
    private final AtomicReference<List<EquipmentState>> cachedEquipmentStates = new AtomicReference<>(new ArrayList<>());

    @PostConstruct
    private void init() {
        updateCache(); // get all equipment data when the service starts
    }

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