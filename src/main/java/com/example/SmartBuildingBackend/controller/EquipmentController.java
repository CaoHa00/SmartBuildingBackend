package com.example.SmartBuildingBackend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping
    public ResponseEntity<EquipmentDto> addEquipment(@RequestParam UUID spaceId, @RequestParam UUID equipmentTypeId, @RequestParam UUID categoryId , @RequestBody EquipmentDto equipmentDto) {
        EquipmentDto newEquipment = equipmentService.addEquipment(spaceId, equipmentTypeId,categoryId,equipmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEquipment);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getAllEquipments() {
        List<EquipmentDto> equipments = equipmentService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }
    
    @GetMapping("/{equipment_id}")
    public ResponseEntity<EquipmentDto> getEquipmentById(@PathVariable("equipment_id") UUID equipmentId) {
        EquipmentDto equipmentDto = equipmentService.getEquipmentById(equipmentId);
        return ResponseEntity.ok(equipmentDto);
    }

    @PutMapping("/{equipment_id}")
    public ResponseEntity<EquipmentDto> updateEquipment(@PathVariable("equipment_id") UUID equipmentId, @RequestBody EquipmentDto updateEquipment) {
        EquipmentDto updatedEquipment = equipmentService.updateEquipment(equipmentId, updateEquipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedEquipment);
    }

    @DeleteMapping("/{equipment_id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable("equipment_id") UUID equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.ok("Equipment deleted successfully");
    }
}
