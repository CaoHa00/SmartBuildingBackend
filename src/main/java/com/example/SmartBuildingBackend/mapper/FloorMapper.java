package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.FloorDto;
import com.example.SmartBuildingBackend.entity.Floor;

public class FloorMapper {
    public static FloorDto mapToFloorDto(Floor floor) {
        return new FloorDto(
                floor.getFloorId(),
                floor.getFloorName(),
                floor.getBlock(),
                floor.getRooms()
                );
    }

    public static Floor mapToFloor(FloorDto floorDto) {
    
        return new Floor(
                floorDto.getFloorId(),
                floorDto.getFloorName(),
                floorDto.getBlock(),
                floorDto.getRooms()
                );
    }
}