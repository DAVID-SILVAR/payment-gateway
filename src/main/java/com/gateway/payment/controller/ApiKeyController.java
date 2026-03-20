package com.gateway.payment.controller;

import com.gateway.payment.dto.ApiKeyRequest;
import com.gateway.payment.dto.ApiKeyResponse;
import com.gateway.payment.enums.ApiKeyEnvironment;
import com.gateway.payment.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    // Cria nova API Key para a empresa
    @PostMapping
    public ResponseEntity<ApiKeyResponse> create(
            @PathVariable Long companyId,
            @Valid @RequestBody ApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(companyId, request));
    }

    // Lista todas as chaves (com filtro opcional por ambiente)
    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> findAll(
            @PathVariable Long companyId,
            @RequestParam(required = false) ApiKeyEnvironment environment) {

        if (environment != null) {
            return ResponseEntity.ok(
                    apiKeyService.findByCompanyIdAndEnvironment(companyId, environment));
        }
        return ResponseEntity.ok(apiKeyService.findByCompanyId(companyId));
    }

    // Busca uma chave por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyResponse> findById(
            @PathVariable Long companyId,
            @PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.findById(id));
    }

    // Revoga uma chave específica
    @DeleteMapping("/{id}/revoke")
    public ResponseEntity<Void> revoke(
            @PathVariable Long companyId,
            @PathVariable Long id) {
        apiKeyService.revoke(companyId, id);
        return ResponseEntity.noContent().build();
    }

    // Revoga todas as chaves da empresa
    @DeleteMapping("/revoke-all")
    public ResponseEntity<Void> revokeAll(@PathVariable Long companyId) {
        apiKeyService.revokeAllByCompany(companyId);
        return ResponseEntity.noContent().build();
    }
}