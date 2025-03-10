package com.example.SmartBuildingBackend.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
    private int equipment_id;

    @Column(name = "equipment_name", nullable = false)
    private String equipment_name;

    @Column(name = "equipment_type", nullable = false)
    private String equipment_type;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "equipment", targetEntity = LogUHoo.class)
    private List<LogUHoo> logUHoos;

    @OneToMany(mappedBy = "equipment", targetEntity = LogAqara.class)
    private List<LogAqara> logAqaras;

}
