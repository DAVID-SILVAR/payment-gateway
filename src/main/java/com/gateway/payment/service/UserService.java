package com.gateway.payment.service;

import com.gateway.payment.entity.User;
import com.gateway.payment.enums.UserStatus;
import com.gateway.payment.dto.UserRequest;
import com.gateway.payment.exception.BusinessException;
import com.gateway.payment.repository.UserRepository;
import com.gateway.payment.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado.");
        }
        if (userRepository.existsByDocument(request.getDocument())) {
            throw new BusinessException("CPF/CNPJ já cadastrado.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .document(request.getDocument())
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    @Transactional
    public User update(Long id, String name, String phone) {
        User user = findById(id);
        user.setName(name);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessException("Senha atual incorreta.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

//    @Transactional
//    public void suspend(Long id) {
//        User user = findById(id);
//        user.setStatus(UserStatus.SUSPENDED);
//        userRepository.save(user);
//    }
}