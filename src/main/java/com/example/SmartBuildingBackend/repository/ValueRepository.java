package com.example.SmartBuildingBackend.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.Value;

public interface ValueRepository extends JpaRepository<Value, UUID> {
}
