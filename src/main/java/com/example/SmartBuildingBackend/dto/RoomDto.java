package com.example.SmartBuildingBackend.dto;


import java.util.List;

import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.Floor;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long roomId;

    @NotBlank(message = "Room name is required")
    private String roomName;
    @NotBlank(message = "Room name is required")
    private Floor floor;
    private List<Equipment> equipments;
}
