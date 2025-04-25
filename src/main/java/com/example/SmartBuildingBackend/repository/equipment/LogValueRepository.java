package com.example.SmartBuildingBackend.repository.equipment;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.equipment.LogValue;

public interface LogValueRepository extends JpaRepository<LogValue, UUID> {
    boolean existsByValueResponseAndValue_ValueIdAndEquipment_EquipmentId(Double valueResponse, UUID valueId, UUID equipmentId);
}
