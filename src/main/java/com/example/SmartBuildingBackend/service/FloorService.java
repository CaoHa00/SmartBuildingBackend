package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.FloorDto;

public interface FloorService {
    List<FloorDto> getAllFloors();
    FloorDto getFloorById(int floorId);
    FloorDto updateFloor(int floorId, FloorDto updateFloor);
    void deleteFloor(int floorId);
    FloorDto addFloor(int blockId,FloorDto floorDto);
    List<FloorDto> getBlockFloors(int blockId);
}
