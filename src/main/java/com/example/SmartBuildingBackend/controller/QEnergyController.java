package com.example.SmartBuildingBackend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.service.QEnergyService;

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
    @GetMapping("/cost_consumption_summary")
    public Map<String,Object> getSiteDataConsupmption() {
        try {
           return qEnergyService.fetchCostConsumptionSummary();
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
}
