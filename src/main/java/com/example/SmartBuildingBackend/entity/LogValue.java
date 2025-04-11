package com.example.SmartBuildingBackend.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @UuidGenerator
    @Column(name = "log_value_id", updatable = false, nullable = false)
    private UUID logValueId;

    @Column(name = "time_stamp")
    private long timeStamp;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    @JsonBackReference(value = "equipment")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "value_id")
    private Value value;

    @Column(name = "value")
    private Double valueResponse; // actual value
}
