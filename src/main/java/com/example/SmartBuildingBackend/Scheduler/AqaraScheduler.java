package com.example.SmartBuildingBackend.Scheduler;

import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.repository.EquipmentRepository;
import com.example.SmartBuildingBackend.service.AqaraService;

@Component
public class AqaraScheduler {

    private final AqaraService aqaraService;
    private final EquipmentRepository equipmentRepository;

    public AqaraScheduler(AqaraService aqaraService, EquipmentRepository equipmentRepository) {
        this.aqaraService = aqaraService;
        this.equipmentRepository = equipmentRepository;
    }

    @Scheduled(fixedRateString = "${aqara.sync.interval:60000}")
    public void autoQueryTemperature() {
        List<Equipment> equipments = equipmentRepository.findByCategory_CategoryName("sensor");
        try {
            String result = aqaraService.fetchAndProcessCurrentValue(equipments);
            System.out.println("Synced data for equipment : " + result);
        } catch (Exception e) {
            System.err.println("Failed to sync equipment : " + e.getMessage());
        }
    }
}