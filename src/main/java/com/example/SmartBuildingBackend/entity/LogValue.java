package com.example.SmartBuildingBackend.entity;

import com.example.SmartBuildingBackend.dto.EquipmentDto;
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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "log_value") // Table name in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logValueId;

    @Column(name = "time_stamp")
    private long timeStamp;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    @JsonBackReference(value = "equipment")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "value_id")
    @JsonBackReference(value = "value")
    private Value value;

    @Column(name = "value")
    private Double valueResponse; // actual value
}
