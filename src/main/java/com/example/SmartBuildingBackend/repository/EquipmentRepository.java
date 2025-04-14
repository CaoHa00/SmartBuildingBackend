package com.example.SmartBuildingBackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.Equipment;
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, UUID> {
    
}
