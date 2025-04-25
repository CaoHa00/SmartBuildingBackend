package com.example.SmartBuildingBackend.dto.equipment;


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
public class ValueDto {
    private UUID valueId;

    @NotBlank(message = "value name is required")
    private String valueName;
}
