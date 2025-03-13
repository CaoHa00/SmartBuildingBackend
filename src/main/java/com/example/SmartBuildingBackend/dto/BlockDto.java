package com.example.SmartBuildingBackend.dto;

import java.util.List;

import com.example.SmartBuildingBackend.entity.Floor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockDto {
    private Long blockId;

    @NotBlank(message = "Block name is required")
    private String blockName;
    
    private List<Floor> floors;
}
