package com.example.SmartBuildingBackend.mapper.campus;

import java.util.ArrayList;
import java.util.List;

import com.example.SmartBuildingBackend.dto.campus.SpaceTypeDto;
import com.example.SmartBuildingBackend.entity.campus.Space;
import com.example.SmartBuildingBackend.entity.campus.SpaceType;

public class SpaceTypeMapper {

    public static SpaceTypeDto mapToDto(SpaceType spaceType) {
        if (spaceType == null) return null;
        return new SpaceTypeDto(
                spaceType.getSpaceTypeId(),
                spaceType.getSpaceTypeName(),
                spaceType.getSpaceLevel(),
                spaceType.getSpaces()
        );
    }

    public static SpaceType mapToEntity(SpaceTypeDto dto) {
        if (dto == null) return null;

        return new SpaceType(
                dto.getSpaceTypeId(),
                dto.getSpaceTypeName(),
                dto.getSpaceLevel(),
                dto.getSpaces()
        );
    }
}
