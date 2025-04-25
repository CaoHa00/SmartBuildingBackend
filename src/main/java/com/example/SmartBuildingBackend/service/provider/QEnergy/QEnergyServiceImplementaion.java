package com.example.SmartBuildingBackend.service.provider.QEnergy;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class QEnergyServiceImplementaion implements QEnergyService{
    private String HOST = "https://admin.qenergy.ai";
    private String LOGIN_ENDPOINT = "/api/auth/login/";
    private String SITE_ENDPOINT = "/api/site/710/";
    private String SITE_ENDPOINT_CONSUMPTION = "/api/site/710/cost_consumption_summary";

    private final WebClient webClient;
    private String accessToken;

    public QEnergyServiceImplementaion(WebClient.Builder webClientBuilder) {
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

        if (siteData != null) {
            System.out.println(siteData);
            System.out.println("Live Power: " + siteData.get("live_power"));
        return siteData;
        } else {
        throw new Exception("Failed to retrieve site data.");
        }
    }
    @Override
    public Map<String,Object> fetchCostConsumptionSummary() throws Exception {
        if( accessToken == null ) {
            getAccessToken();
           }
           
           Map<String, Object> siteData = webClient.get()
            .uri(SITE_ENDPOINT_CONSUMPTION)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(Map.class)
            .block(); 
    
            if (siteData != null) {
            System.out.println("Total Cost " +siteData.get("total_cost") );
            System.out.println("Total consumption " +siteData.get("total_consumption") );
            return siteData;
            } else {
            throw new Exception("Failed to retrieve site data.");
            }
    }
}
