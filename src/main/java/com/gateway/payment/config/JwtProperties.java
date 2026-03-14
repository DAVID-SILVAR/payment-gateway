package com.gateway.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {

    private String secret = "changeme-use-a-strong-secret-in-production-min-256-bits";
    private Long expiration = 86400000L; // 24 horas em ms
    private Long refreshExpiration = 604800000L; // 7 dias em ms
}