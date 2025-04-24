package com.example.SmartBuildingBackend.dto.space;
import java.util.UUID;

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
public class SpaceTuyaDto {
    private UUID id;

    @NotBlank(message = "spaceTuyaPlatFormId is required")
    private Long spaceTuyaPlatFormId;

    @NotBlank(message = "spaceTuyaName is required")
    private String spaceTuyaName;
    @JsonIgnore 
    private UUID spaceId;
}
