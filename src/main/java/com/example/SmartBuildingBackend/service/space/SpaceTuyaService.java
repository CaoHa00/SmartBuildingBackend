package com.example.SmartBuildingBackend.service.space;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.space.SpaceTuyaDto;

public interface SpaceTuyaService {

    SpaceTuyaDto addSpaceTuya(SpaceTuyaDto spaceTuyaDto);

    void deleteSpaceTuya(UUID spaceTuyaId);

    List<SpaceTuyaDto> getAllSpaceTuyas();

    SpaceTuyaDto getSpaceTuyaById(UUID spaceTuyaId);

    SpaceTuyaDto updateSpaceTuya(UUID spaceTuyaId, SpaceTuyaDto spaceTuyaDto);

    SpaceTuyaDto getByPlatformId(Long platformId);
}
