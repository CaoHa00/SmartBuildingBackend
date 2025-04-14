package com.example.SmartBuildingBackend.controller.campus;
import com.example.SmartBuildingBackend.dto.campus.SpaceDto;
import com.example.SmartBuildingBackend.service.campusService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ResponseEntity<SpaceDto> createSpace(@RequestBody SpaceDto dto) {
        return ResponseEntity.ok(spaceService.createSpace(dto));
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
}
