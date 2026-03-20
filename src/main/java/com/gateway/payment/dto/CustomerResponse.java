package com.gateway.payment.dto;

import com.gateway.payment.entity.Customer;
import com.gateway.payment.enums.CustomerStatus;
import com.gateway.payment.enums.DocumentType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private Long companyId;
    private String name;
    private String email;
    private String document;
    private DocumentType documentType;
    private String phone;
    private LocalDate birthDate;
    private CustomerStatus status;
    private AddressResponse address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerResponse fromEntity(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .companyId(customer.getCompany().getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .document(customer.getDocument())
                .documentType(customer.getDocumentType())
                .phone(customer.getPhone())
                .birthDate(customer.getBirthDate())
                .status(customer.getStatus())
                .address(AddressResponse.fromEntity(customer.getAddress()))
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}