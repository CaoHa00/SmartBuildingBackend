package com.example.SmartBuildingBackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.LogValue;

public interface LogValueRepository extends JpaRepository<LogValue, UUID> {

}
