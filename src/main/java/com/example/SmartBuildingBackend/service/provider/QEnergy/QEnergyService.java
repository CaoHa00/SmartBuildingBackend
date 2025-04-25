package com.example.SmartBuildingBackend.service.provider.QEnergy;

import java.util.Map;

public interface QEnergyService {
    String getAccessToken() throws Exception;
    Map<String, Object> fetchSiteData() throws Exception;
    Map<String, Object> fetchCostConsumptionSummary() throws Exception;
}
