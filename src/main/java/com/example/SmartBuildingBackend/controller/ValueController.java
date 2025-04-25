package com.example.SmartBuildingBackend.controller;

import com.example.SmartBuildingBackend.dto.equipment.ValueDto;
import com.example.SmartBuildingBackend.service.equipment.ValueService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/value")
@RequiredArgsConstructor
public class ValueController {

    private final ValueService valueService;

    // Add a new Value
    @PostMapping
    public ResponseEntity<ValueDto> addValue(@RequestBody ValueDto valueDto) {
        ValueDto savedValue = valueService.addValue(valueDto);
        return ResponseEntity.ok(savedValue);
    }

    // Get all Values
    @GetMapping
    public ResponseEntity<List<ValueDto>> getAllValues() {
        List<ValueDto> values = valueService.getAllValues();
        return ResponseEntity.ok(values);
    }

    // Get a Value by ID
    @GetMapping("/{valueId}")
    public ResponseEntity<ValueDto> getValueById(@PathVariable UUID valueId) {
        ValueDto value = valueService.getValueById(valueId);
        return ResponseEntity.ok(value);
    }

    // Update a Value
    @PutMapping("/{valueId}")
    public ResponseEntity<ValueDto> updateValue(
            @PathVariable UUID valueId,
            @RequestBody ValueDto valueDto) {
        ValueDto updatedValue = valueService.updateValue(valueId, valueDto);
        return ResponseEntity.ok(updatedValue);
    }

    // Delete a Value
    @DeleteMapping("/{valueId}")
    public ResponseEntity<String> deleteValue(@PathVariable UUID valueId) {
        valueService.deleteValue(valueId);
        return ResponseEntity.ok("Value deleted successfully.");
    }
}
