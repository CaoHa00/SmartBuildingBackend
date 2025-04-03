package com.example.SmartBuildingBackend.service;

import java.util.List;

import com.example.SmartBuildingBackend.dto.CategoryDto;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long Id);

    CategoryDto getCategoryById(Long Id);

    void deleteCategory(Long Id);
    
    List<CategoryDto> getAllCategories();
}
