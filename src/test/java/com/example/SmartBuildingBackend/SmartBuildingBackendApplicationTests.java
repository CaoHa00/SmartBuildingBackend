package com.example.SmartBuildingBackend;

import com.example.SmartBuildingBackend.Scheduler.TuyaScheduler;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.service.provider.tuya.TuyaService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// class SmartBuildingBackendApplicationTests {

//     @Test
//     void testTuyaSchedulerExecutionTime() {
//         // Mock dependencies
//         TuyaService tuyaService = Mockito.mock(TuyaService.class);
//         EquipmentRepository equipmentRepository = Mockito.mock(EquipmentRepository.class);
//         TuyaScheduler scheduler = new TuyaScheduler(tuyaService, equipmentRepository);

//         long startTime = System.nanoTime();
//         scheduler.autoSyncTuyaDeviceStatus();
//         long endTime = System.nanoTime();

//         long durationMs = (endTime - startTime) / 1_000_000;

//         System.out.println("Execution time of autoSyncTuyaDeviceStatus(): " + durationMs + " ms");
//     }
// }
