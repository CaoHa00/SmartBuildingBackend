package com.example.SmartBuildingBackend.dto;

import java.util.List;

import com.example.SmartBuildingBackend.entity.Floor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockDto {
    private Integer blockId;
    private String blockName;
    private List<Floor> floors;
}
