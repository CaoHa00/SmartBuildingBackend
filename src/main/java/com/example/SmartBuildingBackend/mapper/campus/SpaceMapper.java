package com.example.SmartBuildingBackend.mapper.campus;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
import com.example.SmartBuildingBackend.dto.campus.SpaceDto;
import com.example.SmartBuildingBackend.entity.campus.Space;
import com.example.SmartBuildingBackend.entity.campus.SpaceType;
import com.example.SmartBuildingBackend.mapper.EquipmentMapper;

import java.util.*;
import java.util.stream.Collectors;

public class SpaceMapper {

    // Converts Space entity to SpaceDto with recursion handling
    public static SpaceDto mapToDto(Space space, Set<UUID> visited) {
        if (space == null || visited.contains(space.getSpaceId())) {
            return null;
        }

        visited.add(space.getSpaceId());

        SpaceDto dto = new SpaceDto();
        dto.setSpaceId(space.getSpaceId());
        dto.setSpaceName(space.getSpaceName());
        dto.setSpaceTypeId(space.getSpaceType() != null ? space.getSpaceType().getSpaceTypeId() : null);
        dto.setSpaceTypeName(space.getSpaceType() != null ? space.getSpaceType().getSpaceTypeName() : null);
        dto.setParentId(space.getParent() != null ? space.getParent().getSpaceId() : null);



        // Recursively map children
        dto.setChildren(space.getChildren() != null
                ? space.getChildren().stream()
                    .map(child -> mapToDto(child, visited))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
                : new ArrayList<>());

        return dto;
    }

    // Converts SpaceDto to Space entity
    public static Space mapToEntity(SpaceDto dto, SpaceType spaceType, Space parent) {
        if (dto == null) {
            return null;
        }

        Space space = new Space();
        space.setSpaceId(dto.getSpaceId());
        space.setSpaceName(dto.getSpaceName());
        space.setSpaceType(spaceType);
        space.setParent(parent);
        space.setChildren(new ArrayList<>()); // Always init

        // Note: Equipments are managed elsewhere, not mapped from DTO directly
        return space;
    }
}
