package com.example.SmartBuildingBackend.repository.space;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.space.Space;

import java.util.UUID;

public interface SpaceRepository extends JpaRepository<Space, UUID> {
    
}
