package com.example.SmartBuildingBackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SmartBuildingBackend.dto.LogTuyaDto;
import com.example.SmartBuildingBackend.service.LogTuyaService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/logtuya")
public class LogTuyaController {
    private final LogTuyaService logTuyaService;

    @GetMapping
    public ResponseEntity<List<LogTuyaDto>> getAllLogTuyas() {
        List<LogTuyaDto> logTuyas = logTuyaService.getAllLogTuyas();
        return ResponseEntity.ok(logTuyas);
    }

    @PostMapping("/{equipment_id}")
    public ResponseEntity<LogTuyaDto> addLogTuya(@PathVariable("equipment_id") Long equipmentId,
                                                 @RequestBody LogTuyaDto logTuyaDto) {
        LogTuyaDto newLogTuya = logTuyaService.addLogTuya(equipmentId, logTuyaDto);
        return ResponseEntity.ok(newLogTuya);
    }

    @GetMapping("/{logtuya_id}")
    public ResponseEntity<LogTuyaDto> getLogTuyaById(@PathVariable("logtuya_id") Long logTuyaId) {
        LogTuyaDto logTuya = logTuyaService.getLogTuyaById(logTuyaId);
        return ResponseEntity.ok(logTuya);
    }

    @PutMapping("/{logtuya_id}")
    public ResponseEntity<LogTuyaDto> updateLogTuya(@PathVariable("logtuya_id") Long logTuyaId,
                                                    @RequestBody LogTuyaDto logTuyaDto) {
        LogTuyaDto updatedLogTuya = logTuyaService.updateLogTuya(logTuyaId, logTuyaDto);
        return ResponseEntity.ok(updatedLogTuya);
    }

    @DeleteMapping("/{logtuya_id}")
    public ResponseEntity<Void> deleteLogTuya(@PathVariable("logtuya_id") Long logTuyaId) {
        logTuyaService.deleteLogTuya(logTuyaId);
        return ResponseEntity.ok().build();
    }
}
