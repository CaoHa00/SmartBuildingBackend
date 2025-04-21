package com.example.SmartBuildingBackend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.QEnegery;

public interface QengeryRepository extends JpaRepository<QEnegery, Long> {
   
    List<QEnegery> findAllByTimestampBetween(LocalDate start, LocalDate end);

}
