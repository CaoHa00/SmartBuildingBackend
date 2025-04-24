package com.example.SmartBuildingBackend.service.space;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.space.SpaceDto;
import com.example.SmartBuildingBackend.dto.space.SpaceTypeDto;

public interface SpaceTypeService {

    SpaceTypeDto addSpaceType(SpaceTypeDto spaceTypeDto);

    void deleteSpaceType(UUID spaceTypeId);

    List<SpaceTypeDto> getAllSpaceTypes();

    SpaceTypeDto getSpaceTypeById(UUID spaceTypeId);

    SpaceTypeDto updateSpaceType(UUID spaceTypeId, SpaceTypeDto spaceTypeDto);

    UUID getIdByName(String spaceTypeName);

    Optional<SpaceTypeDto> getSpaceTypeByLevel(long level);
}
