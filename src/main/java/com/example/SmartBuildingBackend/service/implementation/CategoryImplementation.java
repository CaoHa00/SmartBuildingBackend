package com.example.SmartBuildingBackend.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SmartBuildingBackend.dto.CategoryDto;
import com.example.SmartBuildingBackend.entity.Category;
import com.example.SmartBuildingBackend.mapper.CategoryMapper;
import com.example.SmartBuildingBackend.repository.CategoryRepository;
import com.example.SmartBuildingBackend.service.CategoryService;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class CategoryImplementation implements CategoryService  {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.mapToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, UUID Id) {
        Category category = categoryRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Category is not found:" + Id));
        category.setCategoryName(categoryDto.getCategoryName());
        category.setEquipments(categoryDto.getEquipments());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(updatedCategory);
    }

    @Override
    public CategoryDto getCategoryById(UUID Id) {
        Category category = categoryRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Category is not found:" + Id));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public void deleteCategory(UUID Id) {
        Category category = categoryRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Category is not found:" + Id));
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::mapToCategoryDto).toList();
    }
    
}
