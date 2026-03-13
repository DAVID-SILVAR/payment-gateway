package com.gateway.payment.entity;

import com.gateway.payment.enums.CardBrand;
import com.gateway.payment.enums.CardPaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    // Adquirente usado (cielo, rede, stone, stripe)
    @Column(nullable = false)
    private String acquirer;

    // ID da transação no adquirente
    @Column(name = "acquirer_tid")
    private String acquirerTid;

    // Número de parcelas
    @Column(nullable = false)
    @Builder.Default
    private Integer installments = 1;

    @Enumerated(EnumType.STRING)
    private CardBrand brand; // VISA, MASTERCARD, ELO, etc.

    @Column(name = "last_four_digits", length = 4)
    private String lastFourDigits;

    @Column(name = "holder_name")
    private String holderName;

    // Código de autorização do adquirente
    @Column(name = "authorization_code", length = 20)
    private String authorizationCode;

    // Número Sequencial Único
    @Column(length = 20)
    private String nsu;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CardPaymentStatus status = CardPaymentStatus.PENDING;

    @Column(name = "failure_code")
    private String failureCode;

    @Column(name = "failure_message")
    private String failureMessage;

    @Column(name = "captured_at")
    private LocalDateTime capturedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}