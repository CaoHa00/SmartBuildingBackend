package com.example.SmartBuildingBackend.controller.space;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.space.SpaceDto;
import com.example.SmartBuildingBackend.dto.space.SpaceTypeDto;
import com.example.SmartBuildingBackend.entity.LogValue;
import com.example.SmartBuildingBackend.entity.space.Space;
import com.example.SmartBuildingBackend.mapper.space.SpaceMapper;
import com.example.SmartBuildingBackend.service.LogValueService;
import com.example.SmartBuildingBackend.service.TuyaService;
import com.example.SmartBuildingBackend.service.space.*;

import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;
    private final LogValueService logValueService;
    private final TuyaService   tuyaService;
    @PostMapping
    public ResponseEntity<?> createSpace(@RequestBody SpaceDto dto) {
        try {
            SpaceDto created = spaceService.createSpace(dto);
          //  tuyaService.createTuyaSpace(created.getSpaceName(), created.getSpaceId());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Space created successfully",
                    "data", created));
        } catch (BadRequestException e) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceDto> getSpace(@PathVariable UUID id) {
        return ResponseEntity.ok(spaceService.getSpaceById(id));
    }

    @GetMapping
    public ResponseEntity<List<SpaceDto>> getAllSpaces() {
        return ResponseEntity.ok(spaceService.getAllSpaces());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SpaceDto>> getTree() {
        return ResponseEntity.ok(spaceService.getSpacesAsTree());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceDto> updateSpace(@PathVariable UUID id, @RequestBody SpaceDto dto) {
        return ResponseEntity.ok(spaceService.updateSpace(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        spaceService.deleteSpace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{space_id}/status")
    public ResponseEntity<List<Map<String, Object>>> getStatusBySpace(@PathVariable("space_id") UUID spaceId) {
        SpaceDto spaceDto = spaceService.getSpaceById(spaceId);
        List<EquipmentDto> equipmentList = spaceDto.getEquipments();

        List<Map<String, Object>> statusList = new ArrayList<>();

        for (EquipmentDto equipment : equipmentList) {
            List<LogValue> latestStatuses = Optional.ofNullable(
                    logValueService.getLatestStatusList(equipment.getEquipmentId())).orElse(Collections.emptyList());
            for (LogValue log : latestStatuses) {
                Map<String, Object> map = new HashMap<>();
                map.put("valueName", log.getValue().getValueName());
                map.put("valueResponse", log.getValueResponse());
                map.put("equipmentName", equipment.getEquipmentName()); // optional
                map.put("equipmentId", equipment.getEquipmentId());
                statusList.add(map);
            }
        }
        return ResponseEntity.ok(statusList);
    }

}
