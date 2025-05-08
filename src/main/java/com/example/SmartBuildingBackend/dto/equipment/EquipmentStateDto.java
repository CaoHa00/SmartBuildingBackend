package com.example.SmartBuildingBackend.dto.equipment;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentStateDto {
    private UUID equipmentStateId;

    @NotBlank(message = "timestamp is required")
    private long timeStamp;

    @NotNull(message = "Equipment type is required")
    private UUID equipmentId;

    private UUID  valueId;

    @NotNull(message = "value response is required")
    private Double valueResponse;

}
