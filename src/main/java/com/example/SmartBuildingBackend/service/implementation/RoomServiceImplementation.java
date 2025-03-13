package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.RoomDto;
import com.example.SmartBuildingBackend.entity.Floor;
import com.example.SmartBuildingBackend.entity.Room;
import com.example.SmartBuildingBackend.mapper.RoomMapper;
import com.example.SmartBuildingBackend.repository.FloorRepository;
import com.example.SmartBuildingBackend.repository.RoomRepository;
import com.example.SmartBuildingBackend.service.RoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImplementation implements RoomService {
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    @Override
    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(RoomMapper::mapToRoomDto).collect(Collectors.toList());    
    }
    @Override
    public RoomDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        return RoomMapper.mapToRoomDto(room);
    }
    @Override
    public RoomDto updateRoom(Long roomId, RoomDto updateRoom) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        
        room.setRoomName(updateRoom.getRoomName());
        if(updateRoom.getFloor().getFloorId() != null) {
            Floor floor = floorRepository.findById(updateRoom.getFloor().getFloorId())
                    .orElseThrow(() -> new RuntimeException("Floor not found with id: " + updateRoom.getFloor().getFloorId()));
            room.setFloor(floor);;
        }
        Room updatedRoom = roomRepository.save(room);

        return RoomMapper.mapToRoomDto(updatedRoom);
    }
    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        roomRepository.delete(room);
    }
    @Override
    public RoomDto addRoom(Long floorId, RoomDto roomDto) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new RuntimeException("Floor not found with id: " + floorId));
        Room room = new Room();
        room.setRoomName(roomDto.getRoomName());
        room.setFloor(floor);

        Room savedRoom = roomRepository.save(room);
        return RoomMapper.mapToRoomDto(savedRoom); 
    }
}
