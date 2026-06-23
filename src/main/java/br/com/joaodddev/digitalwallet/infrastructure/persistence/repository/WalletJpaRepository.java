package br.com.joaodddev.digitalwallet.infrastructure.persistence.repository;

import br.com.joaodddev.digitalwallet.infrastructure.persistence.entity.WalletJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletJpaEntity, UUID> {
    Optional<WalletJpaEntity> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}