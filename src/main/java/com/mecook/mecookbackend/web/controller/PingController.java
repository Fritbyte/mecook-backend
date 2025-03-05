package com.mecook.mecookbackend.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ping")
public class PingController {
    @GetMapping
    public CompletableFuture<ResponseEntity<String>> ping() {
        return CompletableFuture.completedFuture(ResponseEntity.ok("Server is up and running"));
    }
}
