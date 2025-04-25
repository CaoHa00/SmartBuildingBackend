package com.example.SmartBuildingBackend.repository.equipment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, UUID> {
    List<Equipment> findByCategory_CategoryName(String categoryName);
    List<Equipment> findByEquipmentType_EquipmentTypeName(String equipmentTypeName);
    Equipment findByDeviceId(String deviceId);
    List<Equipment> findByEquipmentIdIn(List<UUID> equipmentIds);
    List<Equipment> findByCategory_CategoryNameAndEquipmentType_EquipmentTypeName(String categoryName, String equipmentTypeName);
}
