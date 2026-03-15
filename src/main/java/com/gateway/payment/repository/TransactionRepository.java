package com.gateway.payment.repository;

import com.gateway.payment.entity.Transaction;
import com.gateway.payment.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByUuid(UUID uuid);

    List<Transaction> findByCompanyId(Long companyId);

    List<Transaction> findByCompanyIdAndStatus(Long companyId, TransactionStatus status);

    List<Transaction> findByCustomerId(Long customerId);
}