package com.example.SmartBuildingBackend.controller.campus;


import com.example.SmartBuildingBackend.dto.campus.SpaceTypeDto;
import com.example.SmartBuildingBackend.service.campusService.SpaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/space-types")
public class SpaceTypeController {

    private final SpaceTypeService spaceTypeService;

    @Autowired
    public SpaceTypeController(SpaceTypeService spaceTypeService) {
        this.spaceTypeService = spaceTypeService;
    }

    @PostMapping
    public ResponseEntity<SpaceTypeDto> addSpaceType(@RequestBody SpaceTypeDto dto) {
        return ResponseEntity.ok(spaceTypeService.addSpaceType(dto));
    }

    @GetMapping
    public ResponseEntity<List<SpaceTypeDto>> getAllSpaceTypes() {
        return ResponseEntity.ok(spaceTypeService.getAllSpaceTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceTypeDto> getSpaceTypeById(@PathVariable UUID id) {
        return ResponseEntity.ok(spaceTypeService.getSpaceTypeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpaceTypeDto> updateSpaceType(@PathVariable UUID id, @RequestBody SpaceTypeDto dto) {
        return ResponseEntity.ok(spaceTypeService.updateSpaceType(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpaceType(@PathVariable UUID id) {
        spaceTypeService.deleteSpaceType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<UUID> getSpaceTypeIdByName(@PathVariable String spaceTypeName) {
        return ResponseEntity.ok(spaceTypeService.getIdByName(spaceTypeName));
    }
}
