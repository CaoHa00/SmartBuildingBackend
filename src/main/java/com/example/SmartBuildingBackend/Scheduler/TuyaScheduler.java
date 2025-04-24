package com.example.SmartBuildingBackend.Scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.repository.EquipmentRepository;
import com.example.SmartBuildingBackend.service.TuyaService;

@Component
public class TuyaScheduler {

    private final TuyaService tuyaService;
    private final EquipmentRepository equipmentRepository;

    public TuyaScheduler(TuyaService tuyaService, EquipmentRepository equipmentRepository) {
        this.tuyaService = tuyaService;
        this.equipmentRepository = equipmentRepository;
    }
    @Scheduled(fixedRateString = "${tuya.sync.interval}")
    public void autoQueryTuyaTemperature() {
        List<Equipment> equipments = equipmentRepository.findByCategory_CategoryNameAndEquipmentType_EquipmentTypeName("sensor","Tuya");
        try {
            String result = tuyaService.getLatestStatusDeviceList(equipments);
            System.out.println("Synced data for equipment : " + result);
        } catch (Exception e) {
            System.err.println("Failed to sync equipment : " + e.getMessage());
        }
    } 
}