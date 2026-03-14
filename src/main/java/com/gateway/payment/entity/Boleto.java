package com.gateway.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "boletos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    // Banco emissor (bradesco, itau, bb, santander)
    @Column(nullable = false)
    private String bank;

    // Nosso número (identificador no banco)
    @Column(name = "our_number")
    private String ourNumber;

    // Código de barras numérico
    @Column(nullable = false)
    private String barcode;

    // Linha digitável (formatada para o usuário)
    @Column(name = "digitable_line", nullable = false)
    private String digitableLine;

    // URL do PDF do boleto
    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    // Multa após vencimento (em centavos)
    @Column(name = "fine_amount")
    @Builder.Default
    private Long fineAmount = 0L;

    // Juros diários após vencimento (em centavos)
    @Column(name = "interest_amount")
    @Builder.Default
    private Long interestAmount = 0L;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // Valor pago (pode diferir por multa/juros)
    @Column(name = "paid_amount")
    private Long paidAmount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isOverdue() {
        return LocalDate.now().isAfter(this.dueDate);
    }
}