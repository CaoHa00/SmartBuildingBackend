package com.example.SmartBuildingBackend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "equipment")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;

    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "equipmentType_id", nullable = false)
    @JsonBackReference(value = "equipmentType-ref")
    private EquipmentType equipmentType;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "equipment")
    private List<LogValue> logValues;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference(value = "category-ref")
    private Category category;
}
