package com.example.SmartBuildingBackend.dto;


import java.util.List;

import com.example.SmartBuildingBackend.entity.Equipment;
import com.example.SmartBuildingBackend.entity.Floor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Integer roomId;
    private String roomName;
    private Floor floor;
    private List<Equipment> equipments;
}
