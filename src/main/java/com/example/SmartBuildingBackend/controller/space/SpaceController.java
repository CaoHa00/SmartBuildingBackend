package com.example.SmartBuildingBackend.controller.space;

import com.example.SmartBuildingBackend.dto.equipment.EquipmentDto;
import com.example.SmartBuildingBackend.dto.equipment.EquipmentStateDto;
import com.example.SmartBuildingBackend.dto.space.SpaceDto;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;
import com.example.SmartBuildingBackend.entity.equipment.LogValue;
import com.example.SmartBuildingBackend.mapper.equipment.EquipmentMapper;
import com.example.SmartBuildingBackend.mapper.equipment.EquipmentStateMapper;
import com.example.SmartBuildingBackend.service.equipment.EquipmentService;
import com.example.SmartBuildingBackend.service.equipment.LogValueService;
import com.example.SmartBuildingBackend.service.provider.tuya.TuyaService;
import com.example.SmartBuildingBackend.service.space.*;

import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;
    private final TuyaService tuyaService;
    private final LogValueService logValueService;

    @PostMapping
    public ResponseEntity<?> createSpace(@RequestBody SpaceDto dto) {
        try {
            SpaceDto created = spaceService.createSpace(dto);
            // tuyaService.createTuyaSpace(created.getSpaceName(), created.getSpaceId());
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

    @PostMapping("/{id}/light-control")
    public ResponseEntity<String> controlLight(@RequestParam int value, @PathVariable UUID id)
            throws Exception {
        String response = tuyaService.controlLight(id, value);
        return ResponseEntity.ok(response);
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
            List<EquipmentState> states = equipment.getEquipmentStates();
            if (states != null) {
                for (EquipmentState state : states) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("valueName", state.getValue().getValueName());
                    map.put("valueResponse", state.getValueResponse());
                    map.put("equipmentName", equipment.getEquipmentName());
                    map.put("equipmentId", equipment.getEquipmentId());
                    map.put("timeStamp", state.getTimeStamp());
                    statusList.add(map);
                }
            }
        }

        return ResponseEntity.ok(statusList);
    }

}