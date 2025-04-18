package com.example.SmartBuildingBackend.dto;
import java.util.List;

import com.example.SmartBuildingBackend.entity.Room;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorDto {
    private Long floorId;
    @NotBlank(message = "Floor name is required")
    private String floorName;
    // @NotBlank(message = "Block ID is required")
    private Long blockId;
    private List<Room> rooms;
}
