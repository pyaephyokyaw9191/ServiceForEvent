package com.serviceforevent.event.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventRequest {
    @NotBlank(message = "Event name is required")
    private String name;

    private String description;

    @NotNull(message = "Event date is required")
    private LocalDateTime eventDate;

    @NotBlank(message = "Event type is required")
    private String eventType;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Expected number of guests is required")
    private Integer expectedGuests;

    @NotNull(message = "Budget is required")
    private BigDecimal budget;

    private Set<Long> requiredServiceIds;
} 