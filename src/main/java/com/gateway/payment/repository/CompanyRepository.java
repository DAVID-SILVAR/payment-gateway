package com.gateway.payment.repository;

import com.gateway.payment.entity.Company;
import com.gateway.payment.enums.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByUserId(Long userId);

    Optional<Company> findByDocument(String document);

    boolean existsByDocument(String document);

    List<Company> findByStatus(CompanyStatus status);

    Optional<Company> findByIdAndUserId(Long id, Long userId);
}