package com.gateway.payment.controller;

import com.gateway.payment.dto.CompanyRequest;
import com.gateway.payment.dto.CompanyResponse;
import com.gateway.payment.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/users/{userId}/companies")
    public ResponseEntity<CompanyResponse> create(
            @PathVariable Long userId,
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.create(userId, request));
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @GetMapping("/users/{userId}/companies")
    public ResponseEntity<List<CompanyResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(companyService.findByUserId(userId));
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.update(id, request));
    }

    @PatchMapping("/companies/{id}/approve")
    public ResponseEntity<CompanyResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.approve(id));
    }

    @PatchMapping("/companies/{id}/suspend")
    public ResponseEntity<CompanyResponse> suspend(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.suspend(id));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}