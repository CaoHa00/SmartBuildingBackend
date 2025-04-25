package com.example.SmartBuildingBackend.entity.equipment;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "value")
public class Value {
    @Id
    @UuidGenerator
    @Column(name = "value_id", updatable = false, nullable = false)
    private UUID valueId;

    @Column(name = "value_name", nullable = false)
    private String valueName;
}
