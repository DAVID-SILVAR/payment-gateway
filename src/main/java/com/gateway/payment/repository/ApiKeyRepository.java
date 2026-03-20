package com.gateway.payment.repository;

import com.gateway.payment.entity.ApiKey;
import com.gateway.payment.enums.ApiKeyStatus;
import com.gateway.payment.enums.ApiKeyEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    // Busca pela chave pública (usada na autenticação de cada request)
    Optional<ApiKey> findByKeyValue(String keyValue);

    // Busca somente chaves ativas (usada no filtro de autenticação)
    Optional<ApiKey> findByKeyValueAndStatus(String keyValue, ApiKeyStatus status);

    List<ApiKey> findByCompanyId(Long companyId);

    // Lista chaves de uma empresa por ambiente
    List<ApiKey> findByCompanyIdAndEnvironment(Long companyId, ApiKeyEnvironment environment);

    List<ApiKey> findByCompanyIdAndStatus(Long companyId, ApiKeyStatus status);

    // Lista chaves ativas de uma empresa por ambiente
    List<ApiKey> findByCompanyIdAndEnvironmentAndStatus(
            Long companyId, ApiKeyEnvironment environment, ApiKeyStatus status);

    // Verifica se a chave já existe (evita duplicidade)
    boolean existsByKeyValue(String keyValue);

    // Atualiza o último uso sem carregar a entidade inteira
    @Modifying
    @Query("UPDATE ApiKey a SET a.lastUsedAt = :lastUsedAt WHERE a.keyValue = :keyValue")
    void updateLastUsedAt(@Param("keyValue") String keyValue, @Param("lastUsedAt") LocalDateTime lastUsedAt);

    // Revoga todas as chaves ativas de uma empresa (ex: ao suspender empresa)
    @Modifying
    @Query("UPDATE ApiKey a SET a.status = 'REVOKED' WHERE a.company.id = :companyId AND a.status = 'ACTIVE'")
    void revokeAllByCompanyId(@Param("companyId") Long companyId);
}