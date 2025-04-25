package com.example.SmartBuildingBackend.entity.space;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

import com.example.SmartBuildingBackend.entity.equipment.Equipment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "spaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Space {

    @Id
    @UuidGenerator
    @Column(name = "space_id", updatable = false, nullable = false)
    private UUID spaceId;

    @Column(nullable = false)
    private String spaceName;

    @ManyToOne
    @JoinColumn(name = "space_type_id", nullable = false)
    private SpaceType spaceType;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference // Prevent recursion
    private Space parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Serialize children in JSON
    private List<Space> children = new ArrayList<>();
    
    @OneToMany(mappedBy = "space",  cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Equipment> equipments;
}
