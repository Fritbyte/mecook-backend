package com.mecook.mecookbackend.infrastructure.persistence.jpa;

import com.mecook.mecookbackend.domain.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryJpaRepository extends JpaRepository<Country, Long> {
    boolean existsByName(String name);
    Optional<Country> findByName(String name);
}
