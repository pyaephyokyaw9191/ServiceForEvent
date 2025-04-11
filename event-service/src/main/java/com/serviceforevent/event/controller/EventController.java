package com.serviceforevent.event.controller;

import com.serviceforevent.common.dto.ApiResponse;
import com.serviceforevent.event.dto.EventRequest;
import com.serviceforevent.event.entity.Event;
import com.serviceforevent.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<Event>> createEvent(
            @Valid @RequestBody EventRequest request,
            @RequestHeader("X-User-ID") Long userId) {
        Event event = eventService.createEvent(request, userId);
        return ResponseEntity.ok(ApiResponse.success("Event created successfully", event));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Event>> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.getEventById(id);
            return ResponseEntity.ok(ApiResponse.success(event));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Event not found with id: " + id));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<Event>>> getEventsByUser(
            @RequestHeader("X-User-ID") Long userId) {
        List<Event> events = eventService.getEventsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/type/{eventType}")
    public ResponseEntity<ApiResponse<List<Event>>> getEventsByType(
            @PathVariable String eventType) {
        List<Event> events = eventService.getEventsByType(eventType);
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Event>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {
        try {
            Event event = eventService.updateEvent(id, request);
            return ResponseEntity.ok(ApiResponse.success("Event updated successfully", event));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Event not found with id: " + id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok(ApiResponse.success("Event deleted successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Event not found with id: " + id));
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> deleteAllEvents() {
        eventService.deleteAllEvents();
        return ResponseEntity.ok(ApiResponse.<Void>success("All events deleted successfully", null));
    }
} 