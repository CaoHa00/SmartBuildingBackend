package com.example.SmartBuildingBackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.dto.provider.QEnergyDto;
import com.example.SmartBuildingBackend.service.provider.QEnergy.QEnergyService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/qenergy")
public class QEnergyController {
    private final QEnergyService qEnergyService;
    
    
    @GetMapping("/site-data")
    public Map<String, Object> getSiteData() {
        try {
            System.out.println("fetch data from controller");
           return qEnergyService.fetchSiteData();
        
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
    @GetMapping("/daily_consumption")
    public ResponseEntity<List<QEnergyDto>> getDailyConsumption() throws Exception {
        List<QEnergyDto> dailyConsumption = qEnergyService.getAllQenergy();
        return ResponseEntity.ok(dailyConsumption);
    }
}
