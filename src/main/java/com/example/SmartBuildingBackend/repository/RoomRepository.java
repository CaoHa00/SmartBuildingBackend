package com.example.SmartBuildingBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.Room;
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    
}
