package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.CountryResponse;
import com.mecook.mecookbackend.domain.exception.CountryNotFoundException;
import com.mecook.mecookbackend.domain.model.Country;
import com.mecook.mecookbackend.domain.repository.CountryRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CountryQueryService {
    private final CountryRepository countryRepository;

    public CountryQueryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Async
    public CompletableFuture<List<CountryResponse>> getAllCountries() {
        List<CountryResponse> responses = countryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }

    @Async
    public CompletableFuture<CountryResponse> getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException("Country not found with id " + id));
        return CompletableFuture.completedFuture(mapToResponse(country));
    }

    @Async
    public CompletableFuture<CountryResponse> getCountryByName(String name) {
        Country country = countryRepository.findByName(name)
                .orElseThrow(() -> new CountryNotFoundException("Country not found with name " + name));
        return CompletableFuture.completedFuture(mapToResponse(country));
    }

    private CountryResponse mapToResponse(Country country) {
        return new CountryResponse(country.getId(), country.getName());
    }
}
