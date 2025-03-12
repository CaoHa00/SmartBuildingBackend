package com.example.SmartBuildingBackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "log_uhoo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogUHoo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "timestamp", nullable = true)
    private long timestamp; // Unix timestamp in milliseconds

    @Column(name = "temperature", nullable = true)
    private double temperature; // Temperature in °C

    @Column(name = "humidity", nullable = true)
    private double humidity; // Humidity in %

    @Column(name = "pm1", nullable = true)
    private double pm1; // PM1 in µg/m³

    @Column(name = "pm25", nullable = true)
    private double pm25; // PM2.5 in µg/m³

    @Column(name = "pm4", nullable = true)
    private double pm4; // PM4 in µg/m³

    @Column(name = "pm10", nullable = true)
    private double pm10; // PM10 in µg/m³

    @Column(name = "ch2o", nullable = true)
    private double ch2o; // CH2O (Formaldehyde) in ppb

    @Column(name = "tvoc", nullable = true)
    private double tvoc; // TVOC (Total Volatile Organic Compounds) in ppb

    @Column(name = "co", nullable = true)
    private double co; // CO in ppm

    @Column(name = "co2", nullable = true)
    private double co2; // CO2 in ppm

    @Column(name = "no2", nullable = true)
    private double no2; // NO2 in ppb

    @Column(name = "ozone", nullable = true)
    private double ozone; // Ozone in ppb

    @Column(name = "light", nullable = true)
    private double light; // Light in lux

    @Column(name = "sound", nullable = true)
    private double sound; // Sound in dBA

    @Column(name = "air_pressure", nullable = true)
    private double airPressure; // Air Pressure in mbar

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    @JsonBackReference
    private Equipment equipment;
}
