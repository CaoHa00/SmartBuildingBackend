package com.example.SmartBuildingBackend.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;
import com.example.SmartBuildingBackend.entity.equipment.Value;
import com.example.SmartBuildingBackend.mapper.equipment.EquipmentMapper;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.service.equipment.EquipmentService;
import com.example.SmartBuildingBackend.service.equipment.EquipmentStateService;
import com.example.SmartBuildingBackend.service.equipment.ValueService;
import com.example.SmartBuildingBackend.service.provider.tuya.TuyaService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class TuyaScheduler {

    @Autowired
    private final TuyaService tuyaService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ValueService valueService;

    @Autowired
    private EquipmentStateService equipmentStateService;

    private List<Equipment> switchLights = equipmentService.getCachedEquipments().stream()
            .filter(e -> "switch".equalsIgnoreCase(e.getCategory().getCategoryName())
                    && "Tuya".equalsIgnoreCase(e.getEquipmentType().getEquipmentTypeName()))
            .toList();

    // @Scheduled(fixedRateString = "${tuya.sync.interval.switchLight}")
    // public void syncSwitchLightDevices() {
    //     Long timeStamp = System.currentTimeMillis();

    //     if (switchLights.isEmpty()) {
    //         switchLights = equipmentService.getCachedEquipments().stream()
    //                 .filter(e -> "Light Switch".equals(e.getCategory().getCategoryName()))
    //                 .toList();
    //     }
    //     List<EquipmentState> switchLightStates = equipmentStateService.getCachedEquipmentStates().stream()
    //             .filter(es -> es.getValue() != null && "light-status".equalsIgnoreCase(es.getValue().getValueName()))
    //             .toList();

    //     List<Value> values = new ArrayList<>();
    //     if (switchLightStates.isEmpty()) {
    //         values = valueService.getCachedValues().stream()
    //                 .filter(e -> "light-status".equalsIgnoreCase(e.getValueName())).toList();
    //     } else {
    //         if (values.size() != 0) {
    //             values.set(0, switchLightStates.get(0).getValue());
    //         } else {
    //             values.add(switchLightStates.get(0).getValue());
    //         }
    //     }
    //     if (switchLights.isEmpty()) {
    //         log.info("No switch lights equipment found to sync.");
    //         return;
    //     }
    //     try {
    //         tuyaService.getStatusLight(switchLights, switchLightStates, timeStamp, values.get(0));
    //         log.info("Synced status for switch lights devices. Total: {}", switchLights.size());
    //     } catch (Exception e) {
    //         log.error("Failed to sync switch lights equipment: {}", e.getMessage(), e);
    //     }
    // }

    private List<Equipment> electricElevators = equipmentService.getCachedEquipments().stream()
            .filter(e -> "electricElevator".equalsIgnoreCase(e.getCategory().getCategoryName())
                    && "Tuya".equalsIgnoreCase(e.getEquipmentType().getEquipmentTypeName()))
            .toList();

    @Scheduled(fixedRateString = "${tuya.sync.interval.elevator}")
    public void syncElevatorDevices() {
        Long timeStamp = System.currentTimeMillis();
        List<EquipmentState> electricElevatorStates = equipmentStateService.getCachedEquipmentStates().stream()
                .filter(es -> "electric-current".equalsIgnoreCase(es.getValue().getValueName())
                        || "active-power".equalsIgnoreCase(es.getValue().getValueName())
                        || "temperature".equalsIgnoreCase(es.getValue().getValueName())
                        || "voltage".equalsIgnoreCase(es.getValue().getValueName()))
                .toList();

        if (electricElevators.isEmpty()) {
            log.info("No elevator equipment found to sync.");
            return;
        }
        List<Value> values = new ArrayList<>();
        if (electricElevatorStates.isEmpty()) {
            values = valueService.getCachedValues();
        }
        try {
            tuyaService.getElevatorElectric(electricElevators, electricElevatorStates, timeStamp,values);
            log.info("Synced properties for elevator devices. Total: {}",
                    electricElevators.size());
        } catch (Exception e) {
            log.error("Failed to sync elevator equipment: {}", e.getMessage(), e);
        }
    }
}
