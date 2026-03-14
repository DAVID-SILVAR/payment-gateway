package com.gateway.payment.service;

import  com.gateway.payment.dto.LoginRequest;
import  com.gateway.payment.dto.LoginResponse;
import  com.gateway.payment.dto.UserRequest;
import  com.gateway.payment.dto.UserResponse;
import com.gateway.payment.entity.User;
import com.gateway.payment.exception.BusinessException;
import  com.gateway.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BusinessException("Email ou senha inválidos");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        return LoginResponse.of(
                token,
                jwtService.getExpirationSeconds(),
                UserResponse.fromEntity(user)
        );
    }

    public UserResponse register(UserRequest request) {
        User user = userService.create(request);
        return UserResponse.fromEntity(user);  // Converte User para UserResponse
    }
}