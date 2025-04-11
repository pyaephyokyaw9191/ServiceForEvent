package com.serviceforevent.provider.service;

import com.serviceforevent.provider.dto.AvailabilityRequest;
import com.serviceforevent.provider.dto.ProviderRegistrationRequest;
import com.serviceforevent.provider.entity.AvailabilitySlot;
import com.serviceforevent.provider.entity.Provider;
import com.serviceforevent.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Provider registerProvider(ProviderRegistrationRequest request) {
        if (providerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Provider provider = new Provider();
        provider.setBusinessName(request.getBusinessName());
        provider.setOwnerName(request.getOwnerName());
        provider.setEmail(request.getEmail());
        provider.setPassword(passwordEncoder.encode(request.getPassword()));
        provider.setPhoneNumber(request.getPhoneNumber());
        provider.setAddress(request.getAddress());
        provider.setDescription(request.getDescription());
        provider.setServiceTypes(request.getServiceTypes());
        provider.setBasePrice(request.getBasePrice());

        return providerRepository.save(provider);
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found with id: " + id));
    }

    public Provider getProviderByEmail(String email) {
        return providerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found"));
    }

    public List<Provider> getProvidersByServiceType(String serviceType) {
        return providerRepository.findByServiceTypesContaining(serviceType);
    }

    public List<Provider> getAllVerifiedProviders() {
        return providerRepository.findAllVerifiedAndActive();
    }

    @Transactional
    public Provider updateProvider(Long id, ProviderRegistrationRequest request) {
        Provider provider = getProviderById(id);
        
        provider.setBusinessName(request.getBusinessName());
        provider.setOwnerName(request.getOwnerName());
        provider.setPhoneNumber(request.getPhoneNumber());
        provider.setAddress(request.getAddress());
        provider.setDescription(request.getDescription());
        provider.setServiceTypes(request.getServiceTypes());
        provider.setBasePrice(request.getBasePrice());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            provider.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return providerRepository.save(provider);
    }

    @Transactional
    public Provider addAvailability(Long id, AvailabilityRequest request) {
        try {
            Provider provider = getProviderById(id);
            
            if (request.getDayOfWeek() == null) {
                throw new IllegalArgumentException("Day of week cannot be null");
            }
            if (request.getStartTime() == null) {
                throw new IllegalArgumentException("Start time cannot be null");
            }
            if (request.getEndTime() == null) {
                throw new IllegalArgumentException("End time cannot be null");
            }
            if (request.getStartTime().isAfter(request.getEndTime())) {
                throw new IllegalArgumentException("Start time must be before end time");
            }
            
            AvailabilitySlot slot = AvailabilitySlot.builder()
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
            
            provider.getAvailabilitySlots().add(slot);
            return providerRepository.save(provider);
        } catch (Exception e) {
            throw new RuntimeException("Error adding availability slot: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Provider updateVerificationStatus(Long id, boolean verified) {
        Provider provider = getProviderById(id);
        provider.setVerified(verified);
        return providerRepository.save(provider);
    }

    @Transactional
    public Provider updateActiveStatus(Long id, boolean active) {
        Provider provider = getProviderById(id);
        provider.setActive(active);
        return providerRepository.save(provider);
    }

    @Transactional
    public Provider updateRating(Long id, Integer rating) {
        Provider provider = getProviderById(id);
        Double currentRating = provider.getRating();
        Integer totalReviews = provider.getTotalReviews();
        
        Double newRating = ((currentRating * totalReviews) + rating) / (totalReviews + 1);
        provider.setRating(newRating);
        provider.setTotalReviews(totalReviews + 1);
        
        return providerRepository.save(provider);
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public Provider createProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }

    public void deleteAllProviders() {
        providerRepository.deleteAll();
    }
} 