package com.example.SmartBuildingBackend.service;

import java.util.Map;

public interface QEnergyService {
    String getAccessToken() throws Exception;
    Map<String, Object> fetchSiteData() throws Exception;
    Map<String, Object> fetchCostConsumptionSummary() throws Exception;
}
