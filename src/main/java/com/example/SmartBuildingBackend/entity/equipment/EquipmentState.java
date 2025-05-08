package com.example.SmartBuildingBackend.entity.equipment;

import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
    name = "equipment_state",
    uniqueConstraints = @UniqueConstraint(columnNames = {"equipment_id", "value_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentState {
    @Id
    @UuidGenerator
    @Column(name = "equipment_state_id", updatable = false, nullable = false)
    private UUID equipmentStateId;

    @Column(name = "time_stamp")
    private long timeStamp;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    @JsonBackReference(value = "equipmentState")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "value_id")
    private Value value;

    @Column(name = "value")
    private Double valueResponse; // actual value
}
