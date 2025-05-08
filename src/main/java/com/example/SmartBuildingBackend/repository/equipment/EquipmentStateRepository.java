package com.example.SmartBuildingBackend.repository.equipment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.equipment.EquipmentState;

import java.util.Optional;
import java.util.UUID;

public interface EquipmentStateRepository extends JpaRepository<EquipmentState, UUID> {

    Optional<EquipmentState> findTopByEquipmentEquipmentId(UUID equipmentId);

    Optional<EquipmentState> findByEquipmentEquipmentIdAndValueValueId(UUID equipmentId, UUID valueId);

}