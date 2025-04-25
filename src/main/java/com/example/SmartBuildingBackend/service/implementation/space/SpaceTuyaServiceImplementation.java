package com.example.SmartBuildingBackend.service.implementation.space;
import com.example.SmartBuildingBackend.dto.space.SpaceTuyaDto;
import com.example.SmartBuildingBackend.entity.space.Space;
import com.example.SmartBuildingBackend.entity.space.SpaceTuya;
import com.example.SmartBuildingBackend.mapper.space.SpaceTuyaMapper;
import com.example.SmartBuildingBackend.repository.space.SpaceRepository;
import com.example.SmartBuildingBackend.repository.space.SpaceTuyaRepository;
import com.example.SmartBuildingBackend.service.space.SpaceTuyaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpaceTuyaServiceImplementation implements SpaceTuyaService {

    private final SpaceTuyaRepository spaceTuyaRepository;
    private final SpaceRepository spaceRepository;

    @Autowired
    public SpaceTuyaServiceImplementation(SpaceTuyaRepository spaceTuyaRepository, SpaceRepository spaceRepository) {
        this.spaceTuyaRepository = spaceTuyaRepository;
        this.spaceRepository = spaceRepository;
    }

    @Override
    public SpaceTuyaDto addSpaceTuya(SpaceTuyaDto spaceTuyaDto) {
        SpaceTuya entity = SpaceTuyaMapper.mapToEntity(spaceTuyaDto);
        SpaceTuya saved = spaceTuyaRepository.save(entity);
        return SpaceTuyaMapper.mapToDto(saved);
    }

    @Override
    public void deleteSpaceTuya(UUID spaceTuyaId) {
        spaceTuyaRepository.deleteById(spaceTuyaId);
    }

    @Override
    public List<SpaceTuyaDto> getAllSpaceTuyas() {
        return spaceTuyaRepository.findAll().stream()
                .map(SpaceTuyaMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SpaceTuyaDto getSpaceTuyaById(UUID spaceTuyaId) {
        return spaceTuyaRepository.findById(spaceTuyaId)
                .map(SpaceTuyaMapper::mapToDto)
                .orElse(null);
    }

    @Override
    public SpaceTuyaDto updateSpaceTuya(UUID spaceTuyaId, SpaceTuyaDto spaceTuyaDto) {
        return spaceTuyaRepository.findById(spaceTuyaId)
                .map(existing -> {
                    existing.setSpaceTuyaPlatFormId(spaceTuyaDto.getSpaceTuyaPlatFormId());

                    Space space = spaceRepository.findById(spaceTuyaDto.getSpaceId()).orElse(null);
                    if (space == null) {
                        throw new IllegalArgumentException("Invalid Space ID");
                    }

                    existing.setSpaceId(space.getSpaceId());
                    SpaceTuya updated = spaceTuyaRepository.save(existing);
                    return SpaceTuyaMapper.mapToDto(updated);
                })
                .orElse(null);
    }

    @Override
    public SpaceTuyaDto getByPlatformId(Long platformId) {
        return spaceTuyaRepository.findBySpaceTuyaPlatFormId(platformId)
                .map(SpaceTuyaMapper::mapToDto)
                .orElse(null);
    }
}