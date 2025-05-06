package com.example.SmartBuildingBackend.Scheduler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.service.provider.tuya.TuyaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TuyaScheduler {

    private final TuyaService tuyaService;
    private final EquipmentRepository equipmentRepository;

    public TuyaScheduler(TuyaService tuyaService, EquipmentRepository equipmentRepository) {
        this.tuyaService = tuyaService;
        this.equipmentRepository = equipmentRepository;
    }

    @Scheduled(fixedRateString = "${tuya.sync.interval}")
    public void autoSyncTuyaDeviceStatus() {
        List<Equipment> equipments = equipmentRepository.findByEquipmentType_EquipmentTypeName("Tuya");

        if (equipments.isEmpty()) {
            log.info("No Tuya equipment found to sync.");
            return;
        }

        try {
            // Partition the equipments into elevators and non-elevators
            Map<Boolean, List<Equipment>> partitionedEquipments = equipments.stream()
                .collect(Collectors.partitioningBy(e -> "electricElevator".equalsIgnoreCase(e.getCategory().getCategoryName())));
    
            List<Equipment> elevators = partitionedEquipments.get(true);
            List<Equipment> nonElevatorDevices = partitionedEquipments.get(false);
    
            // Process non-elevator devices
            if (!nonElevatorDevices.isEmpty()) {
                tuyaService.getLatestStatusDeviceList(nonElevatorDevices);
                log.info("Synced status for non-elevator Tuya devices. Total devices: {}", nonElevatorDevices.size());
            }
    
            // Process elevator devices
            if (!elevators.isEmpty()) {
                tuyaService.getDeviceProperty(elevators);
                log.info("Synced properties for elevator Tuya devices. Total elevators: {}", elevators.size());
            }
    
        } catch (Exception e) {
            log.error("Failed to sync Tuya equipment: {}", e.getMessage(), e);
        }
    }
}
