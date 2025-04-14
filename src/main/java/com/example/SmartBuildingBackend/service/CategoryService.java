package com.example.SmartBuildingBackend.service;

import java.util.List;
import java.util.UUID;

import com.example.SmartBuildingBackend.dto.CategoryDto;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, UUID Id);

    CategoryDto getCategoryById(UUID Id);

    void deleteCategory(UUID Id);
    
    List<CategoryDto> getAllCategories();
}
