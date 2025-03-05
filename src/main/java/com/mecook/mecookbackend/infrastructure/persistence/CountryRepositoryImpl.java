package com.mecook.mecookbackend.infrastructure.persistence;

import com.mecook.mecookbackend.domain.model.Country;
import com.mecook.mecookbackend.domain.repository.CountryRepository;
import com.mecook.mecookbackend.infrastructure.persistence.jpa.CountryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CountryRepositoryImpl implements CountryRepository {
    private final CountryJpaRepository countryJpaRepository;

    public CountryRepositoryImpl(CountryJpaRepository countryJpaRepository) {
        this.countryJpaRepository = countryJpaRepository;
    }

    @Override
    public Optional<Country> findById(Long id) {
        return countryJpaRepository.findById(id);
    }

    @Override
    public Optional<Country> findByName(String name) {
        return countryJpaRepository.findByName(name);
    }

    @Override
    public Country save(Country country) {
        return countryJpaRepository.save(country);
    }

    @Override
    public List<Country> findAll() {
        return countryJpaRepository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return countryJpaRepository.existsByName(name);
    }

    @Override
    public void delete(Country country) {
        countryJpaRepository.delete(country);
    }
}
