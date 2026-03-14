package com.gateway.payment.dto;
import com.gateway.payment.entity.User;
import com.gateway.payment.enums.UserStatus;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String document;
    private UserStatus status;
    private LocalDateTime createdAt;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .document(maskDocument(user.getDocument()))
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // Mascara CPF/CNPJ por segurança
    // CPF:  123.***.***-**
    // CNPJ: 12.***.***/****-**
    private static String maskDocument(String document) {
        if (document == null) return null;
        if (document.length() == 11) {
            return document.substring(0, 3) + ".***.***-**";
        }
        if (document.length() == 14) {
            return document.substring(0, 2) + ".***.***/****-**";
        }
        return document;
    }
}