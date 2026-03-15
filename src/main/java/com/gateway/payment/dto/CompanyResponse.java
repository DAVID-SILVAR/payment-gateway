package com.gateway.payment.dto;

import com.gateway.payment.entity.Company;
import com.gateway.payment.enums.CompanyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;
    private Long userId;
    private String name;
    private String tradeName;
    private String document;
    private String responsibleName;
    private String responsibleDocument;
    private String phone;
    private String email;
    private CompanyStatus status;
    private AddressResponse address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CompanyResponse fromEntity(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .userId(company.getUser().getId())
                .name(company.getName())
                .tradeName(company.getTradeName())
                .document(company.getDocument())
                .responsibleName(company.getResponsibleName())
                .responsibleDocument(company.getResponsibleDocument())
                .phone(company.getPhone())
                .email(company.getEmail())
                .status(company.getStatus())
                .address(AddressResponse.fromEntity(company.getAddress()))
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
}