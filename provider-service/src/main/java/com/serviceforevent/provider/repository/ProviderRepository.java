package com.serviceforevent.provider.repository;

import com.serviceforevent.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT p FROM Provider p JOIN p.serviceTypes st WHERE UPPER(st) = UPPER(:serviceType)")
    List<Provider> findByServiceTypesContaining(@Param("serviceType") String serviceType);
    
    List<Provider> findByActiveTrue();
    
    @Query("SELECT p FROM Provider p WHERE p.verified = true AND p.active = true")
    List<Provider> findAllVerifiedAndActive();
} 