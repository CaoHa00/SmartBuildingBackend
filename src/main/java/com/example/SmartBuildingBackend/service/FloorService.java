package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.FloorDto;

public interface FloorService {
    List<FloorDto> getAllFloors();
    FloorDto getFloorById(Long floorId);
    FloorDto updateFloor(Long floorId, FloorDto updateFloor);
    void deleteFloor(Long floorId);
    FloorDto addFloor(Long blockId,FloorDto floorDto);
}
