package com.example.SmartBuildingBackend.controller.equipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.service.provider.tuya.TuyaService;

@RestController
@RequestMapping("/api/tuya/")
public class TuyaController {
    private TuyaService tuyaService;

    @Autowired
    public TuyaController(TuyaService tuyaService) {
        this.tuyaService = tuyaService;
    }

    // @PostMapping("currentValue")
    // public String getDeviceProperty(@RequestParam UUID equipmentId) {
    //     return tuyaService.getDeviceProperty(equipmentId);
    // }

    @GetMapping("/devices")
    public String getDevicesProperty() {
        return tuyaService.getListDevicesProperty();
    }
}
