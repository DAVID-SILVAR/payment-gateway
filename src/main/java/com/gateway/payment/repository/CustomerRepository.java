package com.gateway.payment.repository;

import com.gateway.payment.entity.Customer;
import com.gateway.payment.enums.CustomerStatus;
import com.gateway.payment.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByCompanyId(Long companyId);

    Optional<Customer> findByCompanyIdAndDocument(Long companyId, String document);

    Optional<Customer> findByCompanyIdAndEmail(Long companyId, String email);

    List<Customer> findByCompanyIdAndStatus(Long companyId, CustomerStatus status);

    List<Customer> findByCompanyIdAndDocumentType(Long companyId, DocumentType documentType);

    boolean existsByCompanyIdAndDocument(Long companyId, String document);

    boolean existsByCompanyIdAndEmail(Long companyId, String email);
}