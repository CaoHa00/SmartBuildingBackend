package com.example.SmartBuildingBackend.repository.campus;


import com.example.SmartBuildingBackend.entity.campus.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpaceRepository extends JpaRepository<Space, UUID> {
    
}
