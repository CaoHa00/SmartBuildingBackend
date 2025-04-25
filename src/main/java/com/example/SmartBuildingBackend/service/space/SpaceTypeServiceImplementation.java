package com.example.SmartBuildingBackend.service.space;
import com.example.SmartBuildingBackend.dto.space.SpaceTypeDto;
import com.example.SmartBuildingBackend.entity.space.SpaceType;
import com.example.SmartBuildingBackend.mapper.space.SpaceTypeMapper;
import com.example.SmartBuildingBackend.repository.space.SpaceTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpaceTypeServiceImplementation implements SpaceTypeService {

    private final SpaceTypeRepository spaceTypeRepository;

    @Autowired
    public SpaceTypeServiceImplementation(SpaceTypeRepository spaceTypeRepository) {
        this.spaceTypeRepository = spaceTypeRepository;
    }

    @Override
    public SpaceTypeDto addSpaceType(SpaceTypeDto spaceTypeDto) {
        long finalLevel = spaceTypeDto.getSpaceLevel();

        if (spaceTypeDto.getSpaceLevel() == 0) {
            // We need to adjust levels for the new `SpaceType`
            // Get all existing SpaceTypes, order by their level
            List<SpaceType> allSpaceTypes = spaceTypeRepository.findAllByOrderBySpaceLevelAsc();

            // If there are existing types, we shift the levels
            if (!allSpaceTypes.isEmpty()) {
                // Update levels of all space types that are at or above the new level
                for (SpaceType existingSpaceType : allSpaceTypes) {
                    if (existingSpaceType.getSpaceLevel() >= 1) {
                        existingSpaceType.setSpaceLevel(existingSpaceType.getSpaceLevel() + 1);
                        spaceTypeRepository.save(existingSpaceType);
                    }
                }
            }

            // Set the new space type to level 1
            finalLevel = 1;
            spaceTypeDto.setSpaceLevel(finalLevel);
        }

        // Now map the DTO to entity and save it
        SpaceType spaceType = SpaceTypeMapper.mapToEntity(spaceTypeDto);
        SpaceType saved = spaceTypeRepository.save(spaceType);

        return SpaceTypeMapper.mapToDto(saved);
    }

    @Override
    public void deleteSpaceType(UUID spaceTypeId) {
        // Step 1: Find the SpaceType before deletion to get its level
        SpaceType spaceTypeToDelete = spaceTypeRepository.findById(spaceTypeId)
                .orElseThrow(() -> new NoSuchElementException("SpaceType with ID " + spaceTypeId + " not found"));

        long deletedSpaceLevel = spaceTypeToDelete.getSpaceLevel();

        // Step 2: Delete the SpaceType
        spaceTypeRepository.deleteById(spaceTypeId);

        // Step 3: Update the levels of affected SpaceTypes
        List<SpaceType> affectedSpaceTypes = spaceTypeRepository
                .findAllBySpaceLevelGreaterThanOrderBySpaceLevelAsc(deletedSpaceLevel);

        long newLevel = deletedSpaceLevel; // Start renumbering from the level of the deleted SpaceType
        for (SpaceType spaceType : affectedSpaceTypes) {
            spaceType.setSpaceLevel(newLevel++);
            spaceTypeRepository.save(spaceType);
        }
    }

    @Override
    public List<SpaceTypeDto> getAllSpaceTypes() {
        return spaceTypeRepository.findAll()
                .stream()
                .map(SpaceTypeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SpaceTypeDto getSpaceTypeById(UUID spaceTypeId) {
        SpaceType spaceType = spaceTypeRepository.findById(spaceTypeId)
                .orElseThrow(() -> new RuntimeException("SpaceType not found with ID: " + spaceTypeId));
        return SpaceTypeMapper.mapToDto(spaceType);
    }

    @Override
    public SpaceTypeDto updateSpaceType(UUID spaceTypeId, SpaceTypeDto spaceTypeDto) {
        SpaceType existing = spaceTypeRepository.findById(spaceTypeId)
                .orElseThrow(() -> new RuntimeException("SpaceType not found with ID: " + spaceTypeId));
        existing.setSpaceTypeName(spaceTypeDto.getSpaceTypeName());
        SpaceType updated = spaceTypeRepository.save(existing);
        return SpaceTypeMapper.mapToDto(updated);
    }

    @Override
    public UUID getIdByName(String spaceTypeName) {
        SpaceType spaceType = spaceTypeRepository.findBySpaceTypeName(spaceTypeName)
                .orElseThrow(() -> new RuntimeException("SpaceType not found with name: " + spaceTypeName));
        return spaceType.getSpaceTypeId();
    }

    public Optional<SpaceTypeDto> getSpaceTypeByLevel(long level) {
        return spaceTypeRepository.findAllBySpaceLevel(level)
            .map(SpaceTypeMapper::mapToDto);
    }
}
