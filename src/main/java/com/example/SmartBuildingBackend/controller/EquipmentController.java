package com.example.SmartBuildingBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.service.EquipmentService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
@RequestMapping("/api/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping("/{roomId}")
    public ResponseEntity<EquipmentDto> addEquipment(@PathVariable int roomId, @RequestBody EquipmentDto equipmentDto) {
        EquipmentDto newEquipment = equipmentService.addEquipment(roomId, equipmentDto);
        return ResponseEntity.ok(newEquipment);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getAllEquipments() {
        List<EquipmentDto> equipments = equipmentService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }
    
    @GetMapping("/{equipment_id}")
    public ResponseEntity<EquipmentDto> getEquipmentById(@PathVariable("equipment_id") int equipmentId) {
        EquipmentDto equipmentDto = equipmentService.getEquipmentById(equipmentId);
        return ResponseEntity.ok(equipmentDto);
    }

    @PutMapping("/{equipment_id}")
    public ResponseEntity<EquipmentDto> updateEquipment(@PathVariable("equipment_id") int equipmentId, @RequestBody EquipmentDto updateEquipment) {
        EquipmentDto updatedEquipment = equipmentService.updateEquipment(equipmentId, updateEquipment);
        return ResponseEntity.ok(updatedEquipment);
    }

    @DeleteMapping("/{equipment_id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable("equipment_id") int equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.ok("Equipment deleted successfully");
    }
}
