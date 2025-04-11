package com.serviceforevent.event.entity;

import com.serviceforevent.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer expectedGuests;

    @Column(nullable = false)
    private BigDecimal budget;

    @ElementCollection
    @CollectionTable(name = "event_services", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "service_id")
    private Set<Long> requiredServiceIds = new HashSet<>();

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String status = "PENDING";
} 