package com.example.SmartBuildingBackend.dto.equipment;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    private UUID categoryId;
    private String categoryName;
    private List<Equipment> equipments; 
}
