package com.example.SmartBuildingBackend.mapper;

import com.example.SmartBuildingBackend.dto.RoomDto;
import com.example.SmartBuildingBackend.entity.Room;

public class RoomMapper {
        public static RoomDto mapToRoomDto(Room room) {
                return new RoomDto(
                                room.getRoomId(),
                                room.getRoomName(),
                                room.getFloor() != null ? room.getFloor().getFloorId() : null,
                                room.getEquipments());
        }

        public static Room mapToRoom(RoomDto roomDto) {
                Room room = new Room();
                room.setEquipments(roomDto.getEquipments());
                room.setRoomId(roomDto.getRoomId());
                room.setRoomName(roomDto.getRoomName());
                return room;
        }
}
