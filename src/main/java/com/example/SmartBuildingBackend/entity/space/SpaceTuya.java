package com.example.SmartBuildingBackend.entity.space;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "space_tuya")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpaceTuya {

    @Id
    @UuidGenerator
    @Column(name = "space_tuya_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Long spaceTuyaPlatFormId;

    @Column(nullable = false)
    private String spaceTuyaName;

    @JoinColumn(name = "space_id")
    private UUID spaceId;

}
