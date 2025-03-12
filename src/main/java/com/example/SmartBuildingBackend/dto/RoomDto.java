package com.example.SmartBuildingBackend.dto;


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
    private Integer room_id;
    private String roomName;
    private Floor floor;
}
