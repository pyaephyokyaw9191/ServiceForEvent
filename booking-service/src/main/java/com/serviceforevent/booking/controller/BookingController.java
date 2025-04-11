package com.serviceforevent.booking.controller;

import com.serviceforevent.booking.dto.BookingRequest;
import com.serviceforevent.booking.entity.Booking;
import com.serviceforevent.booking.entity.Booking.BookingStatus;
import com.serviceforevent.booking.service.BookingService;
import com.serviceforevent.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(
            @Valid @RequestBody BookingRequest request,
            @RequestHeader("X-User-ID") Long userId) {
        if (!bookingService.isProviderAvailable(request.getProviderId(), request.getServiceDate())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Provider is not available at the requested time"));
        }
        Booking booking = bookingService.createBooking(request, userId);
        return ResponseEntity.ok(ApiResponse.success("Booking created successfully", booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByUser(
            @RequestHeader("X-User-ID") Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByProvider(
            @PathVariable Long providerId) {
        List<Booking> bookings = bookingService.getBookingsByProvider(providerId);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByEvent(
            @PathVariable Long eventId) {
        List<Booking> bookings = bookingService.getBookingsByEvent(eventId);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/provider/{providerId}/status/{status}")
    public ResponseEntity<ApiResponse<List<Booking>>> getProviderBookingsByStatus(
            @PathVariable Long providerId,
            @PathVariable BookingStatus status) {
        List<Booking> bookings = bookingService.getProviderBookingsByStatus(providerId, status);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @GetMapping("/user/status/{status}")
    public ResponseEntity<ApiResponse<List<Booking>>> getUserBookingsByStatus(
            @RequestHeader("X-User-ID") Long userId,
            @PathVariable BookingStatus status) {
        List<Booking> bookings = bookingService.getUserBookingsByStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<Booking>> confirmBooking(@PathVariable Long id) {
        Booking booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed successfully", booking));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(
            @PathVariable Long id,
            @RequestParam String reason) {
        Booking booking = bookingService.cancelBooking(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<Booking>> completeBooking(@PathVariable Long id) {
        Booking booking = bookingService.completeBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking completed successfully", booking));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Booking>> rejectBooking(
            @PathVariable Long id,
            @RequestParam String reason) {
        Booking booking = bookingService.rejectBooking(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Booking rejected successfully", booking));
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<ApiResponse<Booking>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam boolean isPaid) {
        Booking booking = bookingService.updatePaymentStatus(id, isPaid);
        return ResponseEntity.ok(ApiResponse.success(
            isPaid ? "Payment completed successfully" : "Payment status updated",
            booking));
    }

    @GetMapping("/provider/{providerId}/availability")
    public ResponseEntity<ApiResponse<Boolean>> checkProviderAvailability(
            @PathVariable Long providerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime serviceDate) {
        boolean isAvailable = bookingService.isProviderAvailable(providerId, serviceDate);
        return ResponseEntity.ok(ApiResponse.success(isAvailable));
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> deleteAllBookings() {
        bookingService.deleteAllBookings();
        return ResponseEntity.ok(ApiResponse.<Void>success("All bookings deleted successfully", null));
    }
} 