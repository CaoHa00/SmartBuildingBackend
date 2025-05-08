package com.example.SmartBuildingBackend.service.provider.QEnergy;

import java.util.List;
import java.util.Map;

import com.example.SmartBuildingBackend.dto.provider.QEnergyDto;

public interface QEnergyService {
    String getAccessToken() throws Exception;
    Map<String, Object> fetchSiteData() throws Exception;
    Map<String, Object> fetchCostConsumptionSummary() throws Exception;
    List<QEnergyDto> getAllQenergy() throws Exception;
}
