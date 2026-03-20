package com.gateway.payment.service;

import com.gateway.payment.dto.ApiKeyRequest;
import com.gateway.payment.dto.ApiKeyResponse;
import com.gateway.payment.entity.ApiKey;
import com.gateway.payment.entity.Company;
import com.gateway.payment.enums.ApiKeyEnvironment;
import com.gateway.payment.enums.ApiKeyStatus;
import com.gateway.payment.enums.CompanyStatus;
import com.gateway.payment.exception.BusinessException;
import com.gateway.payment.exception.ResourceNotFoundException;
import com.gateway.payment.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int MAX_ACTIVE_KEYS_PER_ENV = 5;

    // ─── Criar nova API Key ───────────────────────────────────────────────────

    @Transactional
    public ApiKeyResponse create(Long companyId, ApiKeyRequest request) {
        Company company = companyService.getCompanyById(companyId);

        // Empresa precisa estar ativa
        if (!CompanyStatus.ACTIVE.equals(company.getStatus())) {
            throw new BusinessException("Empresa precisa estar ativa para gerar API Keys");
        }

        // Limite de chaves ativas por ambiente
        long activeKeys = apiKeyRepository
                .findByCompanyIdAndEnvironmentAndStatus(
                        companyId, request.getEnvironment(), ApiKeyStatus.ACTIVE)
                .size();

        if (activeKeys >= MAX_ACTIVE_KEYS_PER_ENV) {
            throw new BusinessException(
                    "Limite de " + MAX_ACTIVE_KEYS_PER_ENV + " chaves ativas por ambiente atingido"
            );
        }

        // Gera chave pública e secret em texto puro
        String rawKeyValue = generateKey(request.getEnvironment(), "pk");
        String rawSecret   = generateKey(request.getEnvironment(), "sk");

        ApiKey apiKey = ApiKey.builder()
                .company(company)
                .keyValue(rawKeyValue)
                .secret(passwordEncoder.encode(rawSecret)) // salva apenas o hash
                .keyName(request.getKeyName())
                .environment(request.getEnvironment())
                .status(ApiKeyStatus.ACTIVE)
                .build();

        apiKeyRepository.save(apiKey);

        log.info("ApiKey criada — company: {} | env: {} | name: {}",
                companyId, request.getEnvironment(), request.getKeyName());

        // Secret retornado apenas na criação — nunca mais recuperável
        return ApiKeyResponse.fromEntityWithSecret(apiKey, rawSecret);
    }

    // ─── Listar por empresa ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ApiKeyResponse> findByCompanyId(Long companyId) {
        return apiKeyRepository.findByCompanyId(companyId)
                .stream()
                .map(ApiKeyResponse::fromEntity)
                .toList();
    }

    // ─── Listar por empresa e ambiente ───────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ApiKeyResponse> findByCompanyIdAndEnvironment(
            Long companyId, ApiKeyEnvironment environment) {
        return apiKeyRepository.findByCompanyIdAndEnvironment(companyId, environment)
                .stream()
                .map(ApiKeyResponse::fromEntity)
                .toList();
    }

    // ─── Buscar por ID ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ApiKeyResponse findById(Long id) {
        return ApiKeyResponse.fromEntity(getApiKeyById(id));
    }

    // ─── Revogar chave ────────────────────────────────────────────────────────

    @Transactional
    public void revoke(Long companyId, Long id) {
        ApiKey apiKey = getApiKeyById(id);

        // Garante que a chave pertence à empresa
        if (!apiKey.getCompany().getId().equals(companyId)) {
            throw new BusinessException("API Key não pertence a esta empresa");
        }

        if (ApiKeyStatus.REVOKED.equals(apiKey.getStatus())) {
            throw new BusinessException("API Key já está revogada");
        }

        apiKey.setStatus(ApiKeyStatus.REVOKED);
        apiKeyRepository.save(apiKey);

        log.info("ApiKey revogada — id: {} | company: {}", id, companyId);
    }

    // ─── Revogar todas as chaves de uma empresa ───────────────────────────────

    @Transactional
    public void revokeAllByCompany(Long companyId) {
        apiKeyRepository.revokeAllByCompanyId(companyId);
        log.info("Todas as ApiKeys revogadas — company: {}", companyId);
    }

    // ─── Registrar último uso (chamado no filtro de autenticação) ─────────────

    @Transactional
    public void registerUsage(String keyValue) {
        apiKeyRepository.updateLastUsedAt(keyValue, LocalDateTime.now());
    }

    // ─── Validar chave (usada no filtro de autenticação via API Key) ──────────

    @Transactional(readOnly = true)
    public ApiKey validateKey(String keyValue) {
        try {
            Optional<ApiKey> apiKey = apiKeyRepository
                    .findByKeyValueAndStatus(keyValue, ApiKeyStatus.ACTIVE);

            if (apiKey.isPresent()) {
                return apiKey.get();
            } else {
                throw new BusinessException("API Key inválida ou revogada");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar API Key", e);
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private ApiKey getApiKeyById(Long id) {
        try {
            Optional<ApiKey> apiKey = apiKeyRepository.findById(id);

            if (apiKey.isPresent()) {
                return apiKey.get();
            } else {
                throw new ResourceNotFoundException("API Key não encontrada: " + id);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar API Key com ID: " + id, e);
        }
    }

    // Gera chave no formato: pk_live_xXxXxXxX ou sk_test_xXxXxXxX
    private String generateKey(ApiKeyEnvironment environment, String prefix) {
        String env = ApiKeyEnvironment.PRODUCTION.equals(environment) ? "live" : "test";
        byte[] bytes = new byte[24];
        SECURE_RANDOM.nextBytes(bytes);
        String random = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return prefix + "_" + env + "_" + random;
    }
}
