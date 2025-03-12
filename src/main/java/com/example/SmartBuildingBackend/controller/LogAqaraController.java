package com.example.SmartBuildingBackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.dto.LogAqaraDto;
import com.example.SmartBuildingBackend.service.LogAqaraService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/logaqara")
public class LogAqaraController {
    public LogAqaraService logAqaraService;

    @GetMapping
    public ResponseEntity<List<LogAqaraDto>> getAllLogAqaras() {
        List<LogAqaraDto> logAqaras = logAqaraService.getAllLogAqaras();
        return ResponseEntity.ok(logAqaras);
    }

    @PostMapping("/{equipment_id}")
    public ResponseEntity<LogAqaraDto> addLogAqara(@PathVariable("equipment_id") int equipmentId,
            @RequestBody LogAqaraDto logAqaraDto) {
        LogAqaraDto newLogAqara = logAqaraService.addLogAqara(equipmentId, logAqaraDto);
        return ResponseEntity.ok(newLogAqara);
    }

    @GetMapping("/{logaqara_id}")
    public ResponseEntity<LogAqaraDto> getLogAqaraById(@PathVariable("logaqara_id") int logAqaraId) {
        LogAqaraDto logAqara = logAqaraService.getLogAqaraById(logAqaraId);
        return ResponseEntity.ok(logAqara);
    }

    @PutMapping("/{logaqara_id}")
    public ResponseEntity<LogAqaraDto> updateLogAqara(@PathVariable("logaqara_id") int logAqaraId,
            @RequestBody LogAqaraDto logAqaraDto) {
        LogAqaraDto updatedLogAqara = logAqaraService.updateLogAqara(logAqaraId, logAqaraDto);
        return ResponseEntity.ok(updatedLogAqara);
    }

    @DeleteMapping("/{logaqara_id}")
    public ResponseEntity<Void> deleteLogAqara(@PathVariable("logaqara_id") int logAqaraId) {
        logAqaraService.deleteLogAqara(logAqaraId);
        return ResponseEntity.ok().build();
    }
}
