package com.example.SmartBuildingBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

}
