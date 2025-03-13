package com.example.SmartBuildingBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.LogTuya;

public interface LogTuyaRepository extends JpaRepository<LogTuya, Long> {

}
