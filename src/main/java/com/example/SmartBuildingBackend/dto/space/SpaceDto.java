package com.example.SmartBuildingBackend.dto.space;
import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.EquipmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDto {
    private UUID spaceId;
    private String spaceName;
    private UUID spaceTypeId;
    private String spaceTypeName; // optional, for display
    private UUID parentId;
    private List<SpaceDto> children; // for tree view
    private List<EquipmentDto> equipments;
}