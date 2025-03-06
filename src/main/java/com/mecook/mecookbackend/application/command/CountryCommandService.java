package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.CountryRequest;
import com.mecook.mecookbackend.application.dto.output.CountryResponse;
import com.mecook.mecookbackend.domain.exception.CountryAlreadyExistsException;
import com.mecook.mecookbackend.domain.exception.CountryNotFoundException;
import com.mecook.mecookbackend.domain.model.Country;
import com.mecook.mecookbackend.domain.repository.CountryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class CountryCommandService {
    private final CountryRepository countryRepository;

    public CountryCommandService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Async
    public CompletableFuture<CountryResponse> createCountry(CountryRequest request) {
        if (countryRepository.existsByName(request.name())) {
            throw new CountryAlreadyExistsException("Country already exists with name: " + request.name());
        }
        Country country = new Country(request.name(), request.imageUrl());
        Country saved = countryRepository.save(country);
        return CompletableFuture.completedFuture(mapToResponse(saved));
    }

    @Async
    public CompletableFuture<CountryResponse> updateCountry(Long id, CountryRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found with id " + id));
        country.setName(request.name());
        country.setImageUrl(request.imageUrl());
        Country updated = countryRepository.save(country);
        return CompletableFuture.completedFuture(mapToResponse(updated));
    }

    @Async
    public CompletableFuture<Void> deleteCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found with id " + id));
        countryRepository.delete(country);
        return CompletableFuture.completedFuture(null);
    }

    private CountryResponse mapToResponse(Country country) {
        return new CountryResponse(country.getId(), country.getName(), country.getImageUrl());
    }
}
