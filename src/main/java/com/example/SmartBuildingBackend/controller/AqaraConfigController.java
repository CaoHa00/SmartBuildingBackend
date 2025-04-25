package com.example.SmartBuildingBackend.controller;

import java.util.List;

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

import com.example.SmartBuildingBackend.dto.AqaraConfigDto;
import com.example.SmartBuildingBackend.service.provider.aqara.AqaraConfigService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/AqaraConfig")
public class AqaraConfigController {
    public final AqaraConfigService aqaraConfigService;

    @GetMapping
    public ResponseEntity<List<AqaraConfigDto>> getAllAqaraConfigs() {
        List<AqaraConfigDto> aqaraConfigs = aqaraConfigService.getAllAqaraConfigs();
        return ResponseEntity.status(HttpStatus.OK).body(aqaraConfigs);
    }
    
    @PostMapping
    public ResponseEntity<AqaraConfigDto> addAqaraConfig(@RequestBody AqaraConfigDto aqaraConfigDto) {
        AqaraConfigDto newAqaraConfig = aqaraConfigService.addAqaraConfig(aqaraConfigDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAqaraConfig);
    }

    @GetMapping("/{aqaraConfig_id}")
    public ResponseEntity<AqaraConfigDto> getAqaraConfigById(@PathVariable("aqaraConfig_id") Long aqaraConfigId) {
        AqaraConfigDto aqaraConfig = aqaraConfigService.getAqaraConfigById(aqaraConfigId);
        return ResponseEntity.ok(aqaraConfig);
    }
    
    @PutMapping("/{aqaraConfig_id}")
    public ResponseEntity<AqaraConfigDto> updateAqaraConfig(@PathVariable("aqaraConfig_id") Long aqaraConfigId,
             @RequestBody AqaraConfigDto aqaraConfigDto) {
        AqaraConfigDto updatedAqaraConfig = aqaraConfigService.updateAqaraConfig(aqaraConfigId, aqaraConfigDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedAqaraConfig);
    }
    @DeleteMapping("/{aqaraConfig_id}")
    public ResponseEntity<Void> deleteAqaraConfig(@PathVariable("aqaraConfig_id") Long aqaraConfigId) {
        aqaraConfigService.deleteAqaraConfig(aqaraConfigId);
        return ResponseEntity.ok().build();
    }
}
