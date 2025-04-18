package com.example.SmartBuildingBackend.repository.campus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.campus.SpaceType;


public interface SpaceTypeRepository extends JpaRepository<SpaceType, UUID> {
    Optional<SpaceType> findBySpaceTypeName(String spaceTypeName);
    List<SpaceType> findAllByOrderBySpaceLevelAsc();
    List<SpaceType> findAllBySpaceLevelGreaterThanOrderBySpaceLevelAsc(long level);
}