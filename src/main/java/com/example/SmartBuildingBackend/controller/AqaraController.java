package com.example.SmartBuildingBackend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.service.AqaraService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/aqara")
public class AqaraController {
    private final AqaraService aqaraService;

    
    @PostMapping("/query-device-info")
    public ResponseEntity<String> queryDeviceInfo(@RequestBody Map<String, Object> requestBody) throws Exception {
            String response = aqaraService.sendRequestToAqara(requestBody);
            return ResponseEntity.ok(response);
       
    }

    @PostMapping("/query-resource-info")
    public ResponseEntity<String> queryResourceInfo(@RequestBody Map<String, Object> requestBody) {
        try {
            // Ensure "data" is present in the request body
            if (!requestBody.containsKey("data")) {
                return ResponseEntity.badRequest().body("Error: 'data' object is missing in the request body.");
            }

            // Extract "model" and "resourceId" from the "data" object
            Object dataObj = requestBody.get("data");
            if (!(dataObj instanceof Map)) {
                return ResponseEntity.badRequest().body("Error: 'data' format is incorrect.");
            }

            Map<String, Object> dataMap = (Map<String, Object>) dataObj;
            String model = (String) dataMap.get("model");
            String resourceId = (String) dataMap.get("resourceId"); 

            if (model == null || model.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: 'model' is required.");
            }

            // Call the service method
            String response = aqaraService.queryDetailsAttributes(requestBody, model, resourceId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


    
}
