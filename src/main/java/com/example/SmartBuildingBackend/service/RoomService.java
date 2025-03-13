package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.RoomDto;

public interface RoomService {
    List<RoomDto> getAllRooms();
    RoomDto getRoomById(Long roomId);
    RoomDto updateRoom(Long roomId, RoomDto updateRoom);
    void deleteRoom(Long roomId);
    RoomDto addRoom(Long floorId,RoomDto roomDto);
}
