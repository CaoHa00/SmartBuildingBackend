package com.example.SmartBuildingBackend.repository.provider;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SmartBuildingBackend.entity.provider.QEnergy;

public interface QEnergyRepository extends JpaRepository<QEnergy, Long> {
        List<QEnergy> findAllByTimestampBetween(LocalDate start, LocalDate end);

    
}
