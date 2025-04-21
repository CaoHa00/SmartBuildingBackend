package com.example.SmartBuildingBackend.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.SmartBuildingBackend.dto.QenergyDto;
import com.example.SmartBuildingBackend.entity.QEnegery;
import com.example.SmartBuildingBackend.mapper.QengeryMapper;
import com.example.SmartBuildingBackend.repository.QengeryRepository;
import com.example.SmartBuildingBackend.service.QEnergyService;

@Service
public class QEnergyServiceImplementaion implements QEnergyService{
    private String HOST = "https://admin.qenergy.ai";
    private String LOGIN_ENDPOINT = "/api/auth/login/";
    private String SITE_ENDPOINT = "/api/site/710/";
    // private String SITE_ENDPOINT_CONSUMPTION = "/api/site/710/cost_consumption_summary";

    private final WebClient webClient;
    private String accessToken;
    private final List<Double> cumulativeConsumptionList = new CopyOnWriteArrayList<>();
    private double totalConsumptionAccumulated = 0.0;
    private final QengeryRepository qengeryRepository;

    public QEnergyServiceImplementaion(WebClient.Builder webClientBuilder, QengeryRepository qengeryRepository) {
        this.qengeryRepository = qengeryRepository;
        this.webClient = webClientBuilder.baseUrl(HOST).build();
    }
    @Override
    public String getAccessToken() throws Exception {
        Map<String ,Object> requestBody = Map.of(
            "role", 0,
            "password", "Qbots2022",
            "email", "eiu@qenergy.ai"
        
        );
        Map<String ,Object> response = webClient.post()
            .uri(LOGIN_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .block();
        if(response != null && response.containsKey("access_token")) {
            accessToken = (String) response.get("access_token");
            return accessToken;
            // System.out.println("Access Token retrieved: " + accessToken);
        } else {
            throw new Exception("Failed to obtain access token");
        }
    }

    @Override
    public Map<String, Object> fetchSiteData() throws Exception {
        // System.out.println("fetchSiteData() called!");
       if( accessToken == null ) {
            System.out.println("Fetching new access token...");
            getAccessToken();
       }
       
       Map<String, Object> siteData = webClient.get()
        .uri(SITE_ENDPOINT)
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .bodyToMono(Map.class)
        .block(); 
        // System.out.println("Site data fetched: " + siteData);
        return siteData;
    }
    public Double calculateCumulativeEnergy(Double powerReadings) {
        double total = 0;
        double intervalHours = 0.5;
        double power = powerReadings; 
        total += power * intervalHours;
        return total;
    }

    @Override
    public Map<String,Object> fetchCostConsumptionSummary() throws Exception {
        if( accessToken == null ) {
            System.out.println("Fetching new access token...");
            getAccessToken();
       }
       
       Map<String, Object> siteData = webClient.get()
        .uri(SITE_ENDPOINT)
        .header("Authorization", "Bearer " + accessToken)
        .retrieve()
        .bodyToMono(Map.class)
        .block(); 

        Double livePower = (Double) siteData.get("live_power");
        Double total = calculateCumulativeEnergy(livePower);
        totalConsumptionAccumulated += Math.round(total * 100.0) / 100.0; 
        
        cumulativeConsumptionList.add(totalConsumptionAccumulated);
        for (Double consumption : cumulativeConsumptionList) {
            System.out.println("Cumulative Consumption: " + consumption);
        }
        QEnegery qEnegery = new QEnegery(LocalDateTime.now(), totalConsumptionAccumulated);
        qengeryRepository.save(qEnegery);
        Map<String, Object> result = Map.of(
            "totalEnergy", totalConsumptionAccumulated
        );
        return result;
    }
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scheduleUpdateEnegery() {
        try {
            fetchCostConsumptionSummary();
        } catch (Exception e) {
            System.err.println("Scheduled fetch failed: " + e.getMessage());
        }
    }
    @Scheduled(cron = "0 0 0 * * ?") 
    public void resetDailyEnergy() {
        totalConsumptionAccumulated = 0.0;
        cumulativeConsumptionList.clear();
        System.out.println(" Energy data reset for new day"); 
    }
   
    @Override
    public List<QenergyDto> getAllQenergy() throws Exception {
       List<QEnegery> qEnegeryList = qengeryRepository.findAll();
       return qEnegeryList.stream().map(QengeryMapper::mapToQenergyDto).toList();
    }
}
