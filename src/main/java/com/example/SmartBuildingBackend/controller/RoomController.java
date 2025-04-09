package com.example.SmartBuildingBackend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.RoomDto;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.LogValue;
import com.example.SmartBuildingBackend.service.RoomService;
import com.example.SmartBuildingBackend.service.LogValueService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private RoomService roomService;
    private LogValueService logValueService;

    @PostMapping("/{floorId}")
    public ResponseEntity<RoomDto> addRoom(@PathVariable Long floorId, @RequestBody RoomDto roomDto) {
        RoomDto newRoom = roomService.addRoom(floorId, roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        List<RoomDto> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{room_id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("room_id") Long roomId) {
        RoomDto roomDto = roomService.getRoomById(roomId);
        return ResponseEntity.ok(roomDto);
    }

    @GetMapping("/{room_id}/status")
    public ResponseEntity<List<Map<String, Object>>> getStatusByRoom(@PathVariable("room_id") Long roomId) {
        RoomDto roomDto = roomService.getRoomById(roomId);
        List<Equipment> equipmentList = roomDto.getEquipments();

        List<Map<String, Object>> statusList = new ArrayList<>();

        for (Equipment equipment : equipmentList) {
            List<LogValue> latestStatuses = logValueService.getLatestStatusList(equipment.getEquipmentId());

            for (LogValue log : latestStatuses) {
                Map<String, Object> map = new HashMap<>();
                map.put("valueName", log.getValue().getValueName());
                map.put("valueResponse", log.getValueResponse());
                statusList.add(map);
            }
        }
        return ResponseEntity.ok(statusList);
    }

    @PutMapping("/{room_id}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable("room_id") Long roomId, @RequestBody RoomDto updateRoom) {
        RoomDto updatedRoom = roomService.updateRoom(roomId, updateRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedRoom);
    }

    @DeleteMapping("/{room_id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("room_id") Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok("Room deleted successfully");
    }
}
