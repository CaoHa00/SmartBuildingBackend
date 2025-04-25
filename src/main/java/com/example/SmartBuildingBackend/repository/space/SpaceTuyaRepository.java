package com.example.SmartBuildingBackend.repository.space;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SmartBuildingBackend.entity.space.SpaceTuya;



public interface SpaceTuyaRepository extends JpaRepository<SpaceTuya, UUID> {
    Optional<SpaceTuya> findBySpaceTuyaPlatFormId(Long platformId);
    Optional<SpaceTuya> findBySpaceId(UUID spaceId);
}