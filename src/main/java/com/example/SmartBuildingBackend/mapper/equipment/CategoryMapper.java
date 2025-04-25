package com.example.SmartBuildingBackend.mapper.equipment;

import com.example.SmartBuildingBackend.dto.equipment.CategoryDto;
import com.example.SmartBuildingBackend.entity.equipment.Category;

public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getEquipments()
        );
    }

    public static Category mapToCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getCategoryId(),
                categoryDto.getCategoryName(),
                categoryDto.getEquipments()
        );
    }
}
