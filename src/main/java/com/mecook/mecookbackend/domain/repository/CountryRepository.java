package com.mecook.mecookbackend.domain.repository;

import com.mecook.mecookbackend.domain.model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryRepository {
    Optional<Country> findById(Long id);
    Optional<Country> findByName(String name);
    Country save(Country country);
    List<Country> findAll();
    boolean existsByName(String name);
    void delete(Country country);
}
