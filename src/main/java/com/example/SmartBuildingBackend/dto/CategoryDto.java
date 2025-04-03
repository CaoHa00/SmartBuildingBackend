package com.example.SmartBuildingBackend.dto;

import java.util.List;

import com.example.SmartBuildingBackend.entity.Equipment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private List<Equipment> equipments; 
}
