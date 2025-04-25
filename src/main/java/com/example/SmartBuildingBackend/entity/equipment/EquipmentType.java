package com.example.SmartBuildingBackend.entity.equipment;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipment_type")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EquipmentType {
    @Id
    @UuidGenerator
    @Column(name = "equipment_type_id", updatable = false, nullable = false)
    private UUID equipmentTypeId;

    @Column(name = "equipment_type_name", nullable = false)
    private String equipmentTypeName;

    @OneToMany(mappedBy = "equipmentType", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "equipmentType-ref")
    private List<Equipment> equipments;
}
