package com.example.SmartBuildingBackend.controller.equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;
import com.example.SmartBuildingBackend.service.equipment.EquipmentStateService;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-states") // Back-end testing only
public class EquipmentStateController {
     @Autowired
    private EquipmentStateService equipmentStateService;

    @GetMapping
    public ResponseEntity<List<EquipmentStateDto>> getAllEquipmentStates() {
        List<EquipmentStateDto> stateList = equipmentStateService.getAllEquipmentStates();
        return ResponseEntity.ok(stateList);
    }
}
