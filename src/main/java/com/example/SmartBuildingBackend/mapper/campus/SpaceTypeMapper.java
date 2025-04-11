package com.example.SmartBuildingBackend.mapper.campus;

import com.example.SmartBuildingBackend.dto.campus.SpaceTypeDto;
import com.example.SmartBuildingBackend.entity.campus.SpaceType;

public class SpaceTypeMapper {

    public static SpaceTypeDto mapToDto(SpaceType spaceType) {
        if (spaceType == null) return null;

        return new SpaceTypeDto(
                spaceType.getSpaceTypeId(),
                spaceType.getSpaceTypeName()
        );
    }

    public static SpaceType mapToEntity(SpaceTypeDto dto) {
        if (dto == null) return null;

        return new SpaceType(
                dto.getSpaceTypeId(),
                dto.getSpaceTypeName()
        );
    }
}
