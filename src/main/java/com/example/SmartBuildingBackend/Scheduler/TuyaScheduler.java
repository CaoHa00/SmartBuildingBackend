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

import jakarta.annotation.PostConstruct;
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

        private List<Equipment> switchLights;
        private List<Equipment> electricElevators;
        private List<EquipmentState> electricElevatorStates;
        private List<EquipmentState> switchLightStates;
        private List<Value> listElevatorValues;
        private List<Value> switchLightValues;

        @PostConstruct
        public void init() {
                List<Equipment> allEquipments = equipmentService.getCachedEquipments();

                this.switchLights = allEquipments.stream()
                                .filter(e -> "switch".equalsIgnoreCase(e.getCategory().getCategoryName())
                                                && "Tuya".equalsIgnoreCase(e.getEquipmentType().getEquipmentTypeName()))
                                .toList();

                this.electricElevators = allEquipments.stream()
                                .filter(e -> "electricElevator".equalsIgnoreCase(e.getCategory().getCategoryName())
                                                && "Tuya".equalsIgnoreCase(e.getEquipmentType().getEquipmentTypeName()))
                                .toList();
                this.electricElevatorStates = equipmentStateService.getCachedEquipmentStates().stream()
                                .filter(es -> "electric-current".equalsIgnoreCase(es.getValue().getValueName())
                                                || "active-power".equalsIgnoreCase(es.getValue().getValueName())
                                                || "temperature".equalsIgnoreCase(es.getValue().getValueName())
                                                || "voltage".equalsIgnoreCase(es.getValue().getValueName())
                                                || "total-energy-consumed"
                                                                .equalsIgnoreCase(es.getValue().getValueName()))
                                .toList();
                this.listElevatorValues = valueService.getCachedValues().stream()
                                .filter(v -> "electric-current".equalsIgnoreCase(v.getValueName())
                                                || "active-power".equalsIgnoreCase(v.getValueName())
                                                || "temperature".equalsIgnoreCase(v.getValueName())
                                                || "voltage".equalsIgnoreCase(v.getValueName())
                                                || "total-energy-consumed".equalsIgnoreCase(v.getValueName()))
                                .toList();
                this.switchLightStates = equipmentStateService.getCachedEquipmentStates().stream()
                                .filter(es -> es.getValue() != null &&
                                                "light-status".equalsIgnoreCase(es.getValue().getValueName()))
                                .toList();
                this.switchLightValues = valueService.getCachedValues().stream()
                                .filter(e -> "light-status".equalsIgnoreCase(e.getValueName())).toList();
        }

        @Scheduled(fixedRateString = "${tuya.sync.interval.switchLight}")
        public void syncSwitchLightDevices() {
                Long timeStamp = System.currentTimeMillis();

                if (switchLights.isEmpty()) {
                        switchLights = equipmentService.getCachedEquipments().stream()
                                        .filter(e -> "Light Switch".equals(e.getCategory().getCategoryName()))
                                        .toList();
                }

                if (switchLights.isEmpty()) {
                        log.info("No switch lights equipment found to sync.");
                        return;
                }
                try {
                        tuyaService.getStatusLight(switchLights, switchLightStates, timeStamp,
                                        switchLightValues.get(0));
                        log.info("Synced status for switch lights devices. Total: {}",
                                        switchLights.size());
                } catch (Exception e) {
                        log.error("Failed to sync switch lights equipment: {}", e.getMessage(), e);
                }
        }

        @Scheduled(fixedRateString = "${tuya.sync.interval.elevator}")
        public void syncElevatorDevices() {
                Long timeStamp = System.currentTimeMillis();

                if (electricElevators.isEmpty()) {
                        log.info("No elevator equipment found to sync.");
                        return;
                }
                try {
                        tuyaService.getElevatorElectric(electricElevators, electricElevatorStates, timeStamp,
                                        listElevatorValues);
                        log.info("Synced properties for elevator devices. Total: {}",
                                        electricElevators.size());
                } catch (Exception e) {
                        log.error("Failed to sync elevator equipment: {}", e.getMessage(), e);
                }
        }
}
