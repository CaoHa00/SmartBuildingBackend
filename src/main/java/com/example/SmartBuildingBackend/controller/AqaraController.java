package com.example.SmartBuildingBackend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.service.AqaraService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    
    @PostMapping("/query-temperature")
    public ResponseEntity<String> queryTemparatureAttributes() {
        try {
            String response = aqaraService.queryTemparatureAttributes();
            ObjectMapper objectMapper = new ObjectMapper();
             JsonNode rootNode = objectMapper.readTree(response);
            ArrayNode resultArray = (ArrayNode) rootNode.get("result");

            // Change the default JSON return from AQARA for readability
            for (JsonNode node : resultArray) {
                JsonNode valueNode = node.get("value");
                String resouceId = node.get("resourceId").asText();
                //write temperature
                if (valueNode != null && resouceId.equals("0.1.85")) {
                    ((ObjectNode) node).put("temperature", valueNode.asText().substring(0,2)); // Copy value
                }
                //write humidity
                if (valueNode != null && resouceId.equals("0.2.85")) {
                    ((ObjectNode) node).put("humidity", valueNode.asText().substring(0,2)); // Copy value
                }
            }
              //      Example JSON return
            //     "result": [
            //         {
            //             "timeStamp": 1742449445425,
            //             "resourceId": "0.1.85",
            //             "value": "2443",
            //             "subjectId": "lumi.54ef441000a4894c",
            //             "temperature": "24" // the value front-end get
            //         },
            //         {
            //             "timeStamp": 1742449445425,
            //             "resourceId": "0.2.85",
            //             "value": "5049",
            //             "subjectId": "lumi.54ef441000a4894c",
            //             "humidity": "50" // the value front-end get
            //         }
            //     ]
            // }
            // Convert back to JSON string
            String updatedResponse = objectMapper.writeValueAsString(rootNode);
            return ResponseEntity.ok(updatedResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
    
}
