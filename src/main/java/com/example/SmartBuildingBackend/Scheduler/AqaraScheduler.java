package com.example.SmartBuildingBackend.Scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.service.provider.aqara.AqaraService;

@Component
public class AqaraScheduler {

    private final AqaraService aqaraService;
    private final EquipmentRepository equipmentRepository;

    public AqaraScheduler(AqaraService aqaraService, EquipmentRepository equipmentRepository) {
        this.aqaraService = aqaraService;
        this.equipmentRepository = equipmentRepository;
    }

    @Scheduled(fixedRateString = "${aqara.sync.interval:1800000}")
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