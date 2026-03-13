package com.gateway.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pix_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PixPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    // ID único gerado para o Banco Central
    @Column(nullable = false, unique = true)
    private String txid;

    // Payload do QR Code (copia e cola)
    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;

    // Imagem base64 do QR Code
    @Column(name = "qr_code_image", columnDefinition = "TEXT")
    private String qrCodeImage;

    // ID fim-a-fim retornado pelo banco após pagamento
    @Column(name = "end_to_end_id")
    private String endToEndId;

    // Dados do pagador (preenchidos após confirmação)
    @Column(name = "payer_document", length = 14)
    private String payerDocument;

    @Column(name = "payer_name")
    private String payerName;

    @Column(name = "payer_bank")
    private String payerBank;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}