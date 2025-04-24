package com.example.SmartBuildingBackend.entity.space;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "space_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpaceType {

    @Id
    @UuidGenerator
    @Column(name = "space_type_id", updatable = false, nullable = false)
    private UUID spaceTypeId;

    @Column(nullable = false, unique = true)
    private String spaceTypeName;

    @Column(name="spaceLevel")
    private Long spaceLevel;

    @OneToMany(mappedBy = "spaceType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Space> spaces = new ArrayList<>();
    
}
