package com.serviceforevent.provider.controller;

import com.serviceforevent.common.dto.ApiResponse;
import com.serviceforevent.provider.dto.AvailabilityRequest;
import com.serviceforevent.provider.dto.ProviderRegistrationRequest;
import com.serviceforevent.provider.entity.Provider;
import com.serviceforevent.provider.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Provider>> registerProvider(
            @Valid @RequestBody ProviderRegistrationRequest request) {
        Provider provider = providerService.registerProvider(request);
        return ResponseEntity.ok(ApiResponse.success("Provider registered successfully", provider));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Provider>> getProviderById(@PathVariable Long id) {
        Provider provider = providerService.getProviderById(id);
        return ResponseEntity.ok(ApiResponse.success(provider));
    }

    @GetMapping("/service-type/{serviceType}")
    public ResponseEntity<ApiResponse<List<Provider>>> getProvidersByServiceType(
            @PathVariable String serviceType) {
        List<Provider> providers = providerService.getProvidersByServiceType(serviceType);
        return ResponseEntity.ok(ApiResponse.success(providers));
    }

    @GetMapping("/verified")
    public ResponseEntity<ApiResponse<List<Provider>>> getAllVerifiedProviders() {
        List<Provider> providers = providerService.getAllVerifiedProviders();
        return ResponseEntity.ok(ApiResponse.success(providers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Provider>> updateProvider(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRegistrationRequest request) {
        Provider provider = providerService.updateProvider(id, request);
        return ResponseEntity.ok(ApiResponse.success("Provider updated successfully", provider));
    }

    @PostMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<Provider>> addAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityRequest request) {
        Provider provider = providerService.addAvailability(id, request);
        return ResponseEntity.ok(ApiResponse.success("Availability added successfully", provider));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<Provider>> verifyProvider(
            @PathVariable Long id,
            @RequestParam boolean verified) {
        Provider provider = providerService.updateVerificationStatus(id, verified);
        return ResponseEntity.ok(ApiResponse.success(
            verified ? "Provider verified successfully" : "Provider verification removed",
            provider));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Provider>> activateProvider(
            @PathVariable Long id,
            @RequestParam boolean active) {
        Provider provider = providerService.updateActiveStatus(id, active);
        return ResponseEntity.ok(ApiResponse.success(
            active ? "Provider activated successfully" : "Provider deactivated successfully",
            provider));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<ApiResponse<Provider>> addRating(
            @PathVariable Long id,
            @RequestParam Integer rating) {
        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Rating must be between 1 and 5"));
        }
        Provider provider = providerService.updateRating(id, rating);
        return ResponseEntity.ok(ApiResponse.success("Rating added successfully", provider));
    }

    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    @PostMapping
    public ResponseEntity<Provider> createProvider(@Valid @RequestBody Provider provider) {
        return ResponseEntity.ok(providerService.createProvider(provider));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<Void>> deleteAllProviders() {
        providerService.deleteAllProviders();
        return ResponseEntity.ok(ApiResponse.<Void>success("All providers deleted successfully", null));
    }
} 