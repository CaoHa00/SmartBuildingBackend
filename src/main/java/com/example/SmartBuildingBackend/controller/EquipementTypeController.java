package com.example.SmartBuildingBackend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.dto.EquipmentTypeDto;
import com.example.SmartBuildingBackend.service.EquipementTypeService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/equipmentType")
public class EquipementTypeController {
    public final EquipementTypeService equipementTypeService;

    @GetMapping
    public ResponseEntity<List<EquipmentTypeDto>> getAllEquipentType() {
        List<EquipmentTypeDto> equipmentTypes = equipementTypeService.getAllEquipmentType();
        return ResponseEntity.status(HttpStatus.OK).body(equipmentTypes);
    }

    @PostMapping
    public ResponseEntity<EquipmentTypeDto> addEquipmentType(@RequestBody EquipmentTypeDto equipmentTypeDto) {
        EquipmentTypeDto newType = equipementTypeService.addEquipmentType(equipmentTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newType);
    }

    @GetMapping("/{equipmentType_id}")
    public ResponseEntity<EquipmentTypeDto> getEquipmentTypeById(@PathVariable("equipmentType_id") UUID Id) {
        EquipmentTypeDto equipmentType = equipementTypeService.getEquipmentTypeById(Id);
        return ResponseEntity.status(HttpStatus.OK).body(equipmentType);
    }

    @PutMapping("/{equipmentType_id}")
    public ResponseEntity<EquipmentTypeDto> updateEquipmentType(@PathVariable("equipmentType_id") UUID Id) {
        equipementTypeService.deleteEquipementType(Id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{equipmentType_id}")
    public ResponseEntity<String> deleteEquipmentType(@PathVariable("equipmentType_id") UUID id) {
        equipementTypeService.deleteEquipementType(id);
        return ResponseEntity.ok("EquipmentType deleted successfully"); // 204 No Content response
    }
}
