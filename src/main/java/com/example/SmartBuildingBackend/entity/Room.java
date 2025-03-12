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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @Column(name = "room_name", nullable = false)
    private String room_name;

    @ManyToOne()
    @JoinColumn(name = "floor_id", nullable = false)
    @JsonBackReference
    private Floor floor;

    @OneToMany(mappedBy = "room",  cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Equipment> equipments;
}
