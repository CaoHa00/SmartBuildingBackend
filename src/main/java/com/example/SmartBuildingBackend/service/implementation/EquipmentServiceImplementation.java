package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.entity.Category;
import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.EquipmentType;
import com.example.SmartBuildingBackend.entity.Room;
import com.example.SmartBuildingBackend.mapper.EquipmentMapper;
import com.example.SmartBuildingBackend.repository.CategoryRepository;
import com.example.SmartBuildingBackend.repository.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.EquipmentTypeRepository;
import com.example.SmartBuildingBackend.repository.RoomRepository;
import com.example.SmartBuildingBackend.service.EquipmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImplementation implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final RoomRepository roomRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EquipmentDto> getAllEquipments() {
        List<Equipment> equipments = equipmentRepository.findAll();
        return equipments.stream().map(EquipmentMapper::mapToEquipmentDto).collect(Collectors.toList());
    }

    @Override
    public EquipmentDto getEquipmentById(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
        return EquipmentMapper.mapToEquipmentDto(equipment);
    }

    @Override
    public EquipmentDto updateEquipment(Long equipmentId, EquipmentDto updateEquipment) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
        equipment.setEquipmentName(updateEquipment.getEquipmentName());

        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return EquipmentMapper.mapToEquipmentDto(updatedEquipment);
    }

    @Override
    public void deleteEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
        equipmentRepository.delete(equipment);
    }

    @Override
    public EquipmentDto addEquipment(Long roomId, Long equipmentTypeId, Long categoryId, EquipmentDto equipmentDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        EquipmentType equipmentType = equipmentTypeRepository.findById(equipmentTypeId)
                .orElseThrow(() -> new RuntimeException("Equipment Type not found with id: " + equipmentTypeId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category  not found with id: " + categoryId));
        Equipment equipment = EquipmentMapper.mapToEquipment(equipmentDto);
        
        equipment.setRoom(room);
        equipment.setEquipmentType(equipmentType);
        equipment.setCategory(category);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return EquipmentMapper.mapToEquipmentDto(savedEquipment);
    }

}
