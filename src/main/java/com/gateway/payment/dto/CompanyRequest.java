package com.gateway.payment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {

    @NotBlank(message = "Razão social é obrigatória")
    private String name;

    private String tradeName;

    @NotBlank(message = "CNPJ é obrigatório")
    private String document;

    // @NotBlank(message = "Nome do responsável é obrigatório")
    private String responsibleName;

    // @NotBlank(message = "CPF do responsável é obrigatório")
    private String responsibleDocument;

    private String phone;

    @Email(message = "Email inválido")
    private String email;

    @Valid
    private AddressRequest address;
}