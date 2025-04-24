package com.example.SmartBuildingBackend.mapper.space;

import com.example.SmartBuildingBackend.dto.space.SpaceTuyaDto;
import com.example.SmartBuildingBackend.entity.space.SpaceTuya;

public class SpaceTuyaMapper {

    public static SpaceTuyaDto mapToDto(SpaceTuya spaceTuya) {
        if (spaceTuya == null) return null;

        return new SpaceTuyaDto(
                spaceTuya.getId(),
                spaceTuya.getSpaceTuyaPlatFormId(),
                spaceTuya.getSpaceTuyaName(),
                spaceTuya.getSpaceId()

        );
    }

    public static SpaceTuya mapToEntity(SpaceTuyaDto dto) {
        if (dto == null) return null;

        return new SpaceTuya(
                dto.getId(),
                dto.getSpaceTuyaPlatFormId(),
                dto.getSpaceTuyaName(),
                dto.getSpaceId()
        );
    }
}
