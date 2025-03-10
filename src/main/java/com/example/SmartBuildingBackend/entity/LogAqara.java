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
@Table(name = "log_aqara") // Table name in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAqara {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long log_id;

    @Column(name = "time_stamp")
    private long time;

    @Column(name = "cost_energy")
    private double costEnergy;

    @Column(name = "load_power")
    private double loadPower;

    @Column(name = "lux")
    private double lux;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "pressure")
    private double pressure;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;
}
