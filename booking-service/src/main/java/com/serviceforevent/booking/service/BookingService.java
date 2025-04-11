package com.serviceforevent.booking.service;

import com.serviceforevent.booking.dto.BookingRequest;
import com.serviceforevent.booking.entity.Booking;
import com.serviceforevent.booking.entity.Booking.BookingStatus;
import com.serviceforevent.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public Booking createBooking(BookingRequest request, Long userId) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setProviderId(request.getProviderId());
        booking.setEventId(request.getEventId());
        booking.setBookingDate(LocalDateTime.now());
        booking.setServiceDate(request.getServiceDate());
        booking.setServiceType(request.getServiceType());
        booking.setAmount(request.getAmount());
        booking.setNotes(request.getNotes());

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByProvider(Long providerId) {
        return bookingRepository.findByProviderId(providerId);
    }

    public List<Booking> getBookingsByEvent(Long eventId) {
        return bookingRepository.findByEventId(eventId);
    }

    public List<Booking> getProviderBookingsByStatus(Long providerId, BookingStatus status) {
        return bookingRepository.findByProviderIdAndStatus(providerId, status);
    }

    public List<Booking> getUserBookingsByStatus(Long userId, BookingStatus status) {
        return bookingRepository.findByUserIdAndStatus(userId, status);
    }

    @Transactional
    public Booking confirmBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long id, String reason) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking completeBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking rejectBooking(Long id, String reason) {
        Booking booking = getBookingById(id);
        booking.setStatus(BookingStatus.REJECTED);
        booking.setCancellationReason(reason);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updatePaymentStatus(Long id, boolean isPaid) {
        Booking booking = getBookingById(id);
        booking.setPaid(isPaid);
        return bookingRepository.save(booking);
    }

    public List<Booking> getProviderBookingsByDateRange(
            Long providerId, LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByServiceDateBetweenAndProviderId(start, end, providerId);
    }

    public boolean isProviderAvailable(Long providerId, LocalDateTime serviceDate) {
        LocalDateTime dayStart = serviceDate.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);
        
        List<Booking> existingBookings = getProviderBookingsByDateRange(providerId, dayStart, dayEnd);
        return existingBookings.stream()
                .noneMatch(booking -> booking.getStatus() != BookingStatus.CANCELLED 
                        && booking.getStatus() != BookingStatus.REJECTED);
    }

    @Transactional
    public void deleteAllBookings() {
        bookingRepository.deleteAll();
        bookingRepository.resetSequence();
    }
} 