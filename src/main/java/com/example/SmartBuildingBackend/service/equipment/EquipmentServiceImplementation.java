package com.example.SmartBuildingBackend.service.equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SmartBuildingBackend.dto.equipment.EquipmentDto;
import com.example.SmartBuildingBackend.entity.equipment.Category;
import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.example.SmartBuildingBackend.entity.equipment.EquipmentType;
import com.example.SmartBuildingBackend.entity.space.Space;
import com.example.SmartBuildingBackend.mapper.equipment.EquipmentMapper;
import com.example.SmartBuildingBackend.repository.equipment.CategoryRepository;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentRepository;
import com.example.SmartBuildingBackend.repository.equipment.EquipmentTypeRepository;
import com.example.SmartBuildingBackend.repository.space.SpaceRepository;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EquipmentServiceImplementation implements EquipmentService {

        @Autowired
        private final EquipmentRepository equipmentRepository;

        @Autowired
        private final EquipmentTypeRepository equipmentTypeRepository;

        @Autowired
        private final SpaceRepository spaceRepository;

        @Autowired
        private final CategoryRepository categoryRepository;

        private final AtomicReference<List<Equipment>> cachedEquipments = new AtomicReference<>(new ArrayList<>());

        @PostConstruct
        private void init() {
                updateCache(); //get all equipment data when the service starts
        }

        @Override
        public EquipmentDto addEquipment(UUID spaceId, UUID equipmentTypeId, UUID categoryId,
                        EquipmentDto equipmentDto) {
                Space space = spaceRepository.findById(spaceId)
                                .orElseThrow(() -> new RuntimeException("Room not found with id: " + spaceId));
                @SuppressWarnings("unused") // used to check if spaceTuya has an Id of spaceId, if not, Fix
                                            // spaceTuyaService or add to TuyaCloud and database

                EquipmentType equipmentType = equipmentTypeRepository.findById(equipmentTypeId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Equipment Type not found with id: " + equipmentTypeId));
                Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

                Equipment equipment = EquipmentMapper.mapToEquipment(equipmentDto, category, equipmentType, space);

                Equipment savedEquipment = equipmentRepository.save(equipment);
                updateCache();
                return EquipmentMapper.mapToEquipmentDto(savedEquipment);
        }

        @Override
        public EquipmentDto updateEquipment(UUID equipmentId, EquipmentDto updateEquipment) {
                Equipment equipment = equipmentRepository.findById(equipmentId)
                                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
                Space space = spaceRepository.findById(updateEquipment.getSpaceId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Room not found with id: " + updateEquipment.getSpaceId()));
                Category category = categoryRepository.findById(updateEquipment.getCategoryId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Category not found with id: " + updateEquipment.getCategoryId()));
                equipment.setEquipmentName(updateEquipment.getEquipmentName());
                equipment.setDeviceId(updateEquipment.getDeviceId());
                equipment.setCategory(category);

                equipment.setSpace(space);
                Equipment updated = equipmentRepository.save(equipment);
                updateCache();
                return EquipmentMapper.mapToEquipmentDto(updated);
        }

        @Override
        public EquipmentDto getEquipmentById(UUID equipmentId) {
                Equipment equipment = equipmentRepository.findById(equipmentId)
                                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
                return EquipmentMapper.mapToEquipmentDto(equipment);
        }

        @Override
        public void deleteEquipment(UUID equipmentId) {
                Equipment equipment = equipmentRepository.findById(equipmentId)
                                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + equipmentId));
                equipmentRepository.delete(equipment);
                updateCache();
        }

        @Override
        public List<EquipmentDto> getAllEquipments() {
                List<Equipment> equipmentList = equipmentRepository.findAll();
                return equipmentList.stream()
                                .map(EquipmentMapper::mapToEquipmentDto)
                                .toList();
        }

        private void updateCache() {
                List<Equipment> updatedEquipments = equipmentRepository.findAll();
                cachedEquipments.set(updatedEquipments); // Set the updated list to cache
        }

        // Get the cached equipment
        @Override
        public List<Equipment> getCachedEquipments() {
                return cachedEquipments.get(); // Thread-safe read of the cache
        }
}
