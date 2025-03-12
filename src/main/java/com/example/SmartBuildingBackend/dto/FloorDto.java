package com.example.SmartBuildingBackend.dto;
import java.util.List;

import com.example.SmartBuildingBackend.entity.Block;
import com.example.SmartBuildingBackend.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorDto {
    private int floorId;
    private String floorName;
    private Block block;
    private List<Room> rooms;
}
