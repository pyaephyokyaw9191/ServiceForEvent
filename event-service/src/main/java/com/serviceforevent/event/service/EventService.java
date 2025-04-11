package com.serviceforevent.event.service;

import com.serviceforevent.event.dto.EventRequest;
import com.serviceforevent.event.entity.Event;
import com.serviceforevent.event.entity.EventType;
import com.serviceforevent.event.repository.EventRepository;
import com.serviceforevent.event.repository.EventTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventTypeRepository eventTypeRepository;

    @Transactional
    public Event createEvent(EventRequest request, Long userId) {
        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setEventType(request.getEventType());
        event.setLocation(request.getLocation());
        event.setExpectedGuests(request.getExpectedGuests());
        event.setBudget(request.getBudget());
        event.setRequiredServiceIds(request.getRequiredServiceIds());
        event.setUserId(userId);
        event.setStatus("PENDING");

        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    public List<Event> getEventsByUser(Long userId) {
        return eventRepository.findByUserId(userId);
    }

    public List<Event> getEventsByType(String eventType) {
        return eventRepository.findByEventType(eventType);
    }

    @Transactional
    public Event updateEvent(Long id, EventRequest request) {
        Event event = getEventById(id);
        
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setEventType(request.getEventType());
        event.setLocation(request.getLocation());
        event.setExpectedGuests(request.getExpectedGuests());
        event.setBudget(request.getBudget());
        event.setRequiredServiceIds(request.getRequiredServiceIds());

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }

    @Transactional
    public void deleteAllEvents() {
        eventRepository.deleteAll();
        eventRepository.resetSequence();
    }
} 