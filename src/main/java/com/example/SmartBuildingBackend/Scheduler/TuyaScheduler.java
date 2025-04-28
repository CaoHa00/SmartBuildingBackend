package com.example.SmartBuildingBackend.Scheduler;

import java.util.ArrayList;
import java.util.List;

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

        if (equipments == null || equipments.isEmpty()) {
            log.info("No Tuya equipment found to sync.");
            return;
        }

        try {
            List<Equipment> elevators = equipmentRepository.findByCategory_CategoryNameAndEquipmentType_EquipmentTypeName("electricElevator", "Tuya");

            if (elevators != null && !elevators.isEmpty()) {
                equipments.removeAll(elevators);
            }

            // Sync normal Tuya devices
            tuyaService.getLatestStatusDeviceList(equipments);
            log.info("Synced status for non-elevator Tuya devices. Total devices: {}", equipments.size());

            // Sync elevator Tuya devices
            if (elevators != null && !elevators.isEmpty()) {
                tuyaService.getDeviceProperty(elevators);
                log.info("Synced properties for elevator Tuya devices. Total elevators: {}", elevators.size());
            }

        } catch (Exception e) {
            log.error("Failed to sync Tuya equipment: {}", e.getMessage(), e);
        }
    }

}