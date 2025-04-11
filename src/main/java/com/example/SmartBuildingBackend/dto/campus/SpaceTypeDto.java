package com.example.SmartBuildingBackend.dto.campus;
import java.util.UUID;

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

}
