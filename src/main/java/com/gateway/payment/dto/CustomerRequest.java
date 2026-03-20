package com.gateway.payment.dto;

import com.gateway.payment.enums.DocumentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    private String document;

    @NotNull(message = "Tipo de documento é obrigatório")
    private DocumentType documentType;

    private String phone;

    private LocalDate birthDate;

    @Valid
    private AddressRequest address;
}