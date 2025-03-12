package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.Room;
import com.example.SmartBuildingBackend.mapper.EquipmentMapper;
import com.example.SmartBuildingBackend.repository.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.RoomRepository;
import com.example.SmartBuildingBackend.service.EquipmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImplementation implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<EquipmentDto> getAllEquipments() {
        List<Equipment> equipments = equipmentRepository.findAll();
        return equipments.stream().map(EquipmentMapper::mapToEquipmentDto).collect(Collectors.toList());    
    }

    @Override
    public EquipmentDto getEquipmentById(int equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
        return EquipmentMapper.mapToEquipmentDto(equipment);
    }

    @Override
    public EquipmentDto updateEquipment(int equipmentId, EquipmentDto updateEquipment) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
        equipment.setEquipmentName(updateEquipment.getEquipmentName());
   
        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return EquipmentMapper.mapToEquipmentDto(updatedEquipment);
    }

    @Override
    public void deleteEquipment(int equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
        equipmentRepository.delete(equipment);
    }

    @Override
    public EquipmentDto addEquipment(int roomId, EquipmentDto equipmentDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        Equipment equipment = new Equipment();
        equipment.setEquipmentName(equipmentDto.getEquipmentName());
        equipment.setEquipmentType(equipmentDto.getEquipmentType());
        equipment.setRoom(room);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return EquipmentMapper.mapToEquipmentDto(savedEquipment); 
    }
}


