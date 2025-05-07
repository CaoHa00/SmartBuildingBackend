package com.example.SmartBuildingBackend.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;

@Component
public class EquipmentScheduler { // for future multi-servers

    // private final EquipmentRepository equipmentRepository;

    // // Thread-safe in-memory storage
    // private final AtomicReference<List<Equipment>> cachedEquipments = new AtomicReference<>(new ArrayList<>());

    // public EquipmentScheduler(EquipmentRepository equipmentRepository) {
    //     this.equipmentRepository = equipmentRepository;
    // }

    // @Scheduled(fixedRateString = "${equipment.sync.interval}") //
    // public void autoQueryTemperature() {
    //     try {
    //         List<Equipment> sensors = equipmentRepository.findByCategory_CategoryName("sensor");
    //         cachedEquipments.set(sensors); // Update thread-safe reference
    //         System.out.println("Updated equipment cache with " + sensors.size() + " items.");
    //     } catch (Exception e) {
    //         System.err.println("Failed to sync equipment: " + e.getMessage());
    //     }
    // }

    // public List<Equipment> getCachedEquipments() {
    //     return cachedEquipments.get(); // Safe read
    // }

    // @PostMapping("/equipment/sync")
    // public ResponseEntity<?> syncNow() {
    // scheduler.autoQueryTemperature();
    // return ResponseEntity.ok("Manual sync triggered");
    // } // public endpoint when added new equipment
}