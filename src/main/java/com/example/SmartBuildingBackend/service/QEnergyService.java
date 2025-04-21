package com.example.SmartBuildingBackend.service;

import java.util.List;
import java.util.Map;

import com.example.SmartBuildingBackend.dto.QenergyDto;

public interface QEnergyService {
    String getAccessToken() throws Exception;
    Map<String, Object> fetchSiteData() throws Exception;
    Map<String, Object> fetchCostConsumptionSummary() throws Exception;

    
    List<QenergyDto> getAllQenergy() throws Exception;
}
