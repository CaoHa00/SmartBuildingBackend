package com.example.SmartBuildingBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.SmartBuildingBackend.dto.FloorDto;
import com.example.SmartBuildingBackend.service.FloorService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/floor")
public class FloorController {
     @Autowired
    private FloorService floorService;

    @PostMapping("/{blockId}")
    public ResponseEntity<FloorDto> addFloor(@PathVariable Long blockId, @RequestBody FloorDto floorDto) {
        FloorDto newFloor = floorService.addFloor(blockId, floorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newFloor);
    }
    @GetMapping
    public ResponseEntity<List<FloorDto>> getAllFloors() {
        List<FloorDto> floors = floorService.getAllFloors();
        return ResponseEntity.ok(floors);
    }
    @GetMapping("/{floor_id}")
    public ResponseEntity<FloorDto> getFloorById(@PathVariable("floor_id") Long id) {
        FloorDto floorDto = floorService.getFloorById(id);
        return ResponseEntity.ok(floorDto);
    }

    @PutMapping("/{floor_id}")
    public ResponseEntity<FloorDto> updateFloor(@PathVariable("floor_id") Long id, @RequestBody FloorDto updateFloor) {
        FloorDto updatedFloor = floorService.updateFloor(id, updateFloor);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedFloor);
    }

    @DeleteMapping("/{floor_id}")
    public ResponseEntity<String> deleteFloor(@PathVariable("floor_id") Long id) {
        floorService.deleteFloor(id);
        return ResponseEntity.ok("Floor deleted successfully");
    }

}