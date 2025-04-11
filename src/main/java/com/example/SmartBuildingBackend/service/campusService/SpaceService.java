package com.example.SmartBuildingBackend.service.campusService;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.campus.SpaceDto;


public interface SpaceService {
    SpaceDto createSpace(SpaceDto dto);
    SpaceDto getSpaceById(UUID id);
    List<SpaceDto> getAllSpaces();
    List<SpaceDto> getSpacesAsTree();
    SpaceDto updateSpace(UUID id, SpaceDto dto);
    void deleteSpace(UUID id);
}
