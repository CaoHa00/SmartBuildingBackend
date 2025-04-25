package com.example.SmartBuildingBackend.repository.equipment;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.equipment.Value;

public interface ValueRepository extends JpaRepository<Value, UUID> {
}
