package com.example.SmartBuildingBackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "log_tuya") // Table name in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogTuya {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "time_stamp")
    private long time;

    @Column(name = "electrical_energy")
    private double electricalEnergy;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;
}
