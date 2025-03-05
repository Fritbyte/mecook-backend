package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.CountryCommandService;
import com.mecook.mecookbackend.application.dto.input.CountryRequest;
import com.mecook.mecookbackend.application.dto.output.CountryResponse;
import com.mecook.mecookbackend.application.query.CountryQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private final CountryCommandService countryCommandService;
    private final CountryQueryService countryQueryService;

    public CountryController(CountryCommandService countryCommandService, CountryQueryService countryQueryService) {
        this.countryCommandService = countryCommandService;
        this.countryQueryService = countryQueryService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CountryResponse>>> getAllCountries() {
        return countryQueryService.getAllCountries()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<CountryResponse>> getCountryById(@PathVariable Long id) {
        return countryQueryService.getCountryById(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<CountryResponse>> getCountryByName(@RequestParam String name) {
        return countryQueryService.getCountryByName(name)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<CountryResponse>> createCountry(@RequestBody @Valid CountryRequest request) {
        return countryCommandService.createCountry(request)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<CountryResponse>> updateCountry(@PathVariable Long id,
                                                                            @RequestBody @Valid CountryRequest request) {
        return countryCommandService.updateCountry(id, request)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteCountry(@PathVariable Long id) {
        return countryCommandService.deleteCountry(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }
}
