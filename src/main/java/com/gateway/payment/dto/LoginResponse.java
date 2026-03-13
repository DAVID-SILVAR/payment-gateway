package com.gateway.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn; // segundos
    // private UserResponse user;

//    public static LoginResponse of(String token, Long expiresIn, UserResponse user) {
//        return LoginResponse.builder()
//                .accessToken(token)
//                .tokenType("Bearer")
//                .expiresIn(expiresIn)
//                .user(user)
//                .build();
//    }
}