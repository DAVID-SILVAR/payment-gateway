package com.gateway.payment.enums;

public enum CardPaymentStatus {
    PENDING,
    AUTHORIZED,     // Autorizado mas não capturado
    CAPTURED,       // Capturado (cobrança confirmada)
    DENIED,         // Negado pela operadora
    CANCELLED,      // Cancelado antes da captura
    REFUNDED        // Estornado
}
