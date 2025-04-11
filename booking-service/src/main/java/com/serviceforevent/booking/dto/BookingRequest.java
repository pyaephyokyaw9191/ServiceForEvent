package com.serviceforevent.booking.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull(message = "Provider ID is required")
    private Long providerId;

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Service date is required")
    private LocalDateTime serviceDate;

    @NotNull(message = "Service type is required")
    private String serviceType;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String notes;
} 