package com.example.SmartBuildingBackend.service.space;

import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;

import com.example.SmartBuildingBackend.dto.space.SpaceDto;


public interface SpaceService {
    SpaceDto createSpace(SpaceDto dto)throws BadRequestException;
    SpaceDto getSpaceById(UUID id);
    List<SpaceDto> getAllSpaces();
    List<SpaceDto> getSpacesAsTree();
    SpaceDto updateSpace(UUID id, SpaceDto dto);
    void deleteSpace(UUID id);
}
