package com.serviceforevent.event.entity;

import com.serviceforevent.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "event_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventType extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "event_type_services", joinColumns = @JoinColumn(name = "event_type_id"))
    @Column(name = "service_type")
    private Set<String> recommendedServiceTypes = new HashSet<>();

    @Column(nullable = false)
    private boolean active = true;
} 