package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.RoomDto;
import com.example.SmartBuildingBackend.entity.Room;

public class RoomMapper {
    public static RoomDto mapToRoomDto(Room room) {
        return new RoomDto(
                room.getRoomId(),
                room.getRoomName(),
                room.getFloor(),
                room.getEquipments()
                );
        }
    
    public static Room mapToRoom (RoomDto roomDto) {
        return new Room(
                roomDto.getRoomId(),
                roomDto.getRoomName(),
                roomDto.getFloor(),
                roomDto.getEquipments()
                );
        }
}
