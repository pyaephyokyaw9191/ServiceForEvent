package com.serviceforevent.booking.entity;

import com.serviceforevent.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseEntity {

    @NotNull
    @Column(nullable = false)
    private Long userId;

    @NotNull
    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private Long eventId;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime serviceDate;

    @Column(nullable = false)
    private String serviceType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private String cancellationReason;

    @Column(nullable = false)
    private boolean isPaid = false;

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED,
        REJECTED
    }
} 