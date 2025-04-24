package com.example.SmartBuildingBackend.dto.space;
import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.entity.space.Space;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpaceTypeDto {
    private UUID spaceTypeId;

    @NotBlank(message = "value name is required")
    private String spaceTypeName;

    @NotBlank(message = "space level is required, 1 is the highest level. If you input 0, your next SpaceType will be the highest")
    private Long spaceLevel;
    
    @JsonIgnore 
    private List<Space> spaces;
}
