package com.example.SmartBuildingBackend.entity.campus;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

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
}
