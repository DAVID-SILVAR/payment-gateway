package com.gateway.payment.entity;

import com.gateway.payment.enums.ApiKeyEnvironment;
import com.gateway.payment.enums.ApiKeyStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "key_value", nullable = false, unique = true)
    private String keyValue; // pk_live_xxx ou pk_test_xxx

    @Column(nullable = false)
    private String secret;   // sk_live_xxx ou sk_test_xxx (armazenar hash)

    @Column(name = "key_name")
    private String keyName;  // Nome amigável ex: "Produção", "Homologação"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApiKeyEnvironment environment = ApiKeyEnvironment.SANDBOX;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApiKeyStatus status = ApiKeyStatus.ACTIVE;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}