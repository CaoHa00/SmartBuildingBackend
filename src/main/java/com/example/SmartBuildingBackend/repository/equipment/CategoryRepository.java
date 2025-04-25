package com.example.SmartBuildingBackend.repository.equipment;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.equipment.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
   
}
