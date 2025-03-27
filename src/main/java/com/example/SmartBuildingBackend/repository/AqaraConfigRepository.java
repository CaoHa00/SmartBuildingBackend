package com.example.SmartBuildingBackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartBuildingBackend.entity.AqaraConfig;
@Repository
public interface AqaraConfigRepository extends JpaRepository<AqaraConfig, Long> {
    Optional<AqaraConfig> findFirstByOrderByAqaraConfigIdDesc();

}
