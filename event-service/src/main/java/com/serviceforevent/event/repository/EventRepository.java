package com.serviceforevent.event.repository;

import com.serviceforevent.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUserId(Long userId);
    List<Event> findByEventType(String eventType);
    
    @Modifying
    @Query(value = "ALTER TABLE events ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetSequence();
} 