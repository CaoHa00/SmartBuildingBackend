package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.RoomDto;

public interface RoomService {
    List<RoomDto> getAllRooms();
    RoomDto getRoomById(int roomId);
    RoomDto updateRoom(int roomId, RoomDto updateRoom);
    void deleteRoom(int roomId);
    RoomDto addRoom(int floorId,RoomDto roomDto);
}
