package com.example.SmartBuildingBackend.service.implementation.campusImplementation;

import com.example.SmartBuildingBackend.dto.campus.SpaceTypeDto;
import com.example.SmartBuildingBackend.entity.campus.SpaceType;
import com.example.SmartBuildingBackend.mapper.campus.SpaceTypeMapper;
import com.example.SmartBuildingBackend.repository.campus.SpaceTypeRepository;
import com.example.SmartBuildingBackend.service.campusService.SpaceTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        SpaceType spaceType = SpaceTypeMapper.mapToEntity(spaceTypeDto);
        SpaceType saved = spaceTypeRepository.save(spaceType);
        return SpaceTypeMapper.mapToDto(saved);
    }

    @Override
    public void deleteSpaceType(UUID spaceTypeId) {
        spaceTypeRepository.deleteById(spaceTypeId);
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
}
