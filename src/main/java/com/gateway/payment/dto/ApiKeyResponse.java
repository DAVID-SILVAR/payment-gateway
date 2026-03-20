package com.gateway.payment.dto;

import com.gateway.payment.entity.ApiKey;
import com.gateway.payment.enums.ApiKeyStatus;
import com.gateway.payment.enums.ApiKeyEnvironment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKeyResponse {

    private Long id;
    private Long companyId;
    private String keyValue;
    private String secret;          // Retornado APENAS na criação — nunca depois
    private String keyName;
    private ApiKeyEnvironment environment;
    private ApiKeyStatus status;
    private LocalDateTime lastUsedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    // Para listagem — secret nunca é exposto
    public static ApiKeyResponse fromEntity(ApiKey apiKey) {
        return ApiKeyResponse.builder()
                .id(apiKey.getId())
                .companyId(apiKey.getCompany().getId())
                .keyValue(apiKey.getKeyValue())
                .secret(null)
                .keyName(apiKey.getKeyName())
                .environment(apiKey.getEnvironment())
                .status(apiKey.getStatus())
                .lastUsedAt(apiKey.getLastUsedAt())
                .expiresAt(apiKey.getExpiresAt())
                .createdAt(apiKey.getCreatedAt())
                .build();
    }

    // Para criação — secret retornado em texto puro uma única vez
    public static ApiKeyResponse fromEntityWithSecret(ApiKey apiKey, String rawSecret) {
        return ApiKeyResponse.builder()
                .id(apiKey.getId())
                .companyId(apiKey.getCompany().getId())
                .keyValue(apiKey.getKeyValue())
                .secret(rawSecret)
                .keyName(apiKey.getKeyName())
                .environment(apiKey.getEnvironment())
                .status(apiKey.getStatus())
                .expiresAt(apiKey.getExpiresAt())
                .createdAt(apiKey.getCreatedAt())
                .build();
    }
}