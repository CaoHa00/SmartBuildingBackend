package com.example.SmartBuildingBackend.dto;


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
    private Long valueId;

    @NotBlank(message = "value name is required")
    private String valueName;


}
