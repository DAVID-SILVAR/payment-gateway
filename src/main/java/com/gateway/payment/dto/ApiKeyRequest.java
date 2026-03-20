package com.gateway.payment.dto;

import com.gateway.payment.enums.ApiKeyEnvironment;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKeyRequest {

    @NotNull(message = "Ambiente é obrigatório")
    private ApiKeyEnvironment environment;

    // Nome amigável para identificar a chave ex: "Produção App Mobile"
    private String keyName;
}