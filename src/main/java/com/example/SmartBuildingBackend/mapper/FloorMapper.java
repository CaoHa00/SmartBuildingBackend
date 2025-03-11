package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.FloorDto;
import com.example.SmartBuildingBackend.entity.Block;
import com.example.SmartBuildingBackend.entity.Floor;
import com.example.SmartBuildingBackend.repository.BlockRepository;

public class FloorMapper {
    public static FloorDto mapToFloorDto(Floor floor) {
        return new FloorDto(
                floor.getFloor_id(),
                floor.getFloor_name(),
                floor.getBlock().getBlock_id(),
                floor.getRooms());
    }

    public static Floor mapToFloor(FloorDto floorDto, BlockRepository blockRepository) {
        Block block = blockRepository.findById(floorDto.getBlock_id()) // Fetch Block entity
                .orElseThrow(() -> new RuntimeException("Block not found with id: " + floorDto.getBlock_id()));

        return new Floor(
                floorDto.getFloor_id(),
                floorDto.getFloorName(),
                block, // ✅ Pass the actual Block object
                floorDto.getRooms());
    }
}