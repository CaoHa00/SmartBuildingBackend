package com.example.SmartBuildingBackend.controller;

import com.example.SmartBuildingBackend.dto.LogValueDto;
import com.example.SmartBuildingBackend.service.LogValueService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/logValue")
@RequiredArgsConstructor
public class LogValueController {

    private final LogValueService logValueService;

    // Add a new LogValue
    @PostMapping
    public ResponseEntity<LogValueDto> addLogValue(@RequestParam UUID equipmentId, @RequestParam UUID valueId,
            @RequestBody LogValueDto logValueDto) {
        LogValueDto savedLogValue = logValueService.addLogValue(equipmentId, valueId, logValueDto);

        return ResponseEntity.ok(savedLogValue);
    }

    // Get all LogValues
    @GetMapping
    public ResponseEntity<List<LogValueDto>> getAllLogValues() {
        List<LogValueDto> logValues = logValueService.getAllLogValues();
        return ResponseEntity.ok(logValues);
    }

    // Get a LogValue by ID
    @GetMapping("/{logValueId}")
    public ResponseEntity<LogValueDto> getLogValueById(@PathVariable UUID logValueId) {
        LogValueDto logValue = logValueService.getLogValueById(logValueId);
        return ResponseEntity.ok(logValue);
    }

    // Update a LogValue
    @PutMapping("/{logValueId}")
    public ResponseEntity<LogValueDto> updateLogValue(@PathVariable UUID logValueId, @RequestBody LogValueDto logValueDto) {
        LogValueDto updatedLogValue = logValueService.updateLogValue(logValueId, logValueDto);
        return ResponseEntity.ok(updatedLogValue);
    }

    // Delete a LogValue
    @DeleteMapping("/{logValueId}")
    public ResponseEntity<String> deleteLogValue(@PathVariable UUID logValueId) {
        logValueService.deleteLogValue(logValueId);
        return ResponseEntity.ok("LogValue deleted successfully.");
    }

}
