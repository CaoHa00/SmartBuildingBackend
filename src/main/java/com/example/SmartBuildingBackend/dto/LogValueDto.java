package com.example.SmartBuildingBackend.dto;

import com.example.SmartBuildingBackend.entity.Value;

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
public class LogValueDto {
    private Long logValueId;

    @NotBlank(message = "timestamp is required")
    private long timeStamp;

    @NotNull(message = "Equipment type is required")
    private Long equipmentId;

    @NotNull(message = "value is required")
    private Value value;

    @NotNull(message = "value response is required")
    private Double valueResponse;

}
