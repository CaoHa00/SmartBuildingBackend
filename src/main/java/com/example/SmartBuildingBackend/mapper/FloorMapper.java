package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.FloorDto;
import com.example.SmartBuildingBackend.entity.Floor;


public class FloorMapper {
    public static FloorDto mapToFloorDto(Floor floor) {
        return new FloorDto(
                floor.getFloorId(),
                floor.getFloorName(),
                floor.getBlock()  != null ? floor.getBlock().getBlockId() : null,
                floor.getRooms()
                );
    }

    public static Floor mapToFloor(FloorDto floorDto) {
        Floor floor = new Floor();
            floor.setFloorId(floorDto.getFloorId());
            floor.setFloorName(floorDto.getFloorName());
            floor.setRooms(floorDto.getRooms());
        return floor;
    }
}