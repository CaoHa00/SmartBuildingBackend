package com.example.SmartBuildingBackend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.service.AqaraService;

@RestController
@RequestMapping("/api/aqara")
public class AqaraController {
    private final AqaraService aqaraService;

    public AqaraController(AqaraService aqaraService) {
        this.aqaraService = aqaraService;
    }

    // @GetMapping("/send")
    // public String sendAqaraRequest() throws Exception {
    //     return aqaraService.sendRequestToAqara();
    // }

    @PostMapping("/query-device-info")
    public ResponseEntity<String> queryDeviceInfo(@RequestBody Map<String, Object> requestBody) throws Exception {
            String response = aqaraService.sendRequestToAqara(requestBody);
            return ResponseEntity.ok(response);
       
    }
}
