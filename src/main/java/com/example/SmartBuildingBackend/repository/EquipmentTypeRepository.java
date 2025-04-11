package com.example.SmartBuildingBackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.EquipmentType;

@Repository
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, UUID> {

}
