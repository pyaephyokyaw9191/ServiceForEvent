package com.serviceforevent.booking.repository;

import com.serviceforevent.booking.entity.Booking;
import com.serviceforevent.booking.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByProviderId(Long providerId);
    List<Booking> findByEventId(Long eventId);
    List<Booking> findByProviderIdAndStatus(Long providerId, BookingStatus status);
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
    List<Booking> findByServiceDateBetweenAndProviderId(
        LocalDateTime start, LocalDateTime end, Long providerId);
    
    @Modifying
    @Query(value = "ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetSequence();
} 