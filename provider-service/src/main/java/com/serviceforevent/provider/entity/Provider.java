package com.serviceforevent.provider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "providers")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String businessName;

    @NotBlank
    @Column(nullable = false)
    private String ownerName;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column
    private String description;

    @Column(name = "business_hours")
    private String businessHours;

    @Column(name = "service_area")
    private String serviceArea;

    @ElementCollection
    @CollectionTable(
        name = "provider_services",
        joinColumns = @JoinColumn(name = "provider_id")
    )
    @Column(name = "service_type")
    private Set<String> serviceTypes = new HashSet<>();

    @Column(nullable = false)
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean verified = false;

    @Column(nullable = false)
    private boolean active = true;

    @ElementCollection
    @CollectionTable(
        name = "provider_availability",
        joinColumns = @JoinColumn(name = "provider_id")
    )
    private Set<AvailabilitySlot> availabilitySlots = new HashSet<>();

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private int totalReviews = 0;
}
 