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
                floor.getBlock()
                );
    }

    public static Floor mapToFloor(FloorDto floorDto) {
    
        return new Floor(
                floorDto.getFloor_id(),
                floorDto.getFloorName(),
                floorDto.getBlock()
                );
    }
}