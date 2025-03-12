package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.RoomDto;
import com.example.SmartBuildingBackend.entity.Room;

public class RoomMapper {
    public static RoomDto mapToRoomDto(Room room) {
        return new RoomDto(
                room.getRoom_id(),
                room.getRoom_name(),
                room.getFloor()
                );
        }
    
    public static Room mapToRoom (RoomDto roomDto) {
        return new Room(
                roomDto.getRoom_id(),
                roomDto.getRoomName(),
                roomDto.getFloor()
                );
        }
}
