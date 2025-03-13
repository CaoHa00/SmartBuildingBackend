package com.example.SmartBuildingBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.Floor;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {

    
}