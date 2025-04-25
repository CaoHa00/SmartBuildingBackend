package com.example.SmartBuildingBackend.Scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.service.provider.tuya.TuyaService;

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
        List<Equipment> equipments = equipmentRepository.findByEquipmentType_EquipmentTypeName("Tuya");
        try {
            @SuppressWarnings("unused") // for debugger log
            String result = tuyaService.getLatestStatusDeviceList(equipments);
          //  System.out.println("Synced data for equipment : " + result); log for debugging
        } catch (Exception e) {
            System.err.println("Failed to sync equipment : " + e.getMessage());
        }
    } 
}