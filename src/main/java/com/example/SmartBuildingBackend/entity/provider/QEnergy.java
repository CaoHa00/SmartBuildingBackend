package com.example.SmartBuildingBackend.entity.provider;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "QEnergy")
public class QEnergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "cumulative_consumption", nullable = false)   
    private double cumulativeEnergy;   

    
   public QEnergy(LocalDateTime timestamp, double cumulativeEnergy) {
        this.timestamp = timestamp;
        this.cumulativeEnergy = cumulativeEnergy;
    }   
}
