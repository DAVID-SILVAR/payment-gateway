package com.gateway.payment.enums;

public enum TransactionStatus {
    PENDING,        // Aguardando pagamento
    PROCESSING,     // Em processamento
    PAID,           // Pago com sucesso
    FAILED,         // Falhou
    REFUNDED,       // Estornado totalmente
    PARTIALLY_REFUNDED, // Estornado parcialmente
    CHARGEBACK,     // Chargeback aberto
    EXPIRED,        // Expirou sem pagamento
    CANCELLED       // Cancelado
}
