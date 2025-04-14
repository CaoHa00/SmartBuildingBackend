package com.example.SmartBuildingBackend.service.campusService;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.campus.SpaceTypeDto;

public interface SpaceTypeService {

    SpaceTypeDto addSpaceType(SpaceTypeDto spaceTypeDto);

    void deleteSpaceType(UUID spaceTypeId);

    List<SpaceTypeDto> getAllSpaceTypes();

    SpaceTypeDto getSpaceTypeById(UUID spaceTypeId);

    SpaceTypeDto updateSpaceType(UUID spaceTypeId, SpaceTypeDto spaceTypeDto);

    UUID getIdByName(String spaceTypeName);
}
