package br.com.joaodddev.digitalwallet.infrastructure.persistence.repository;

import br.com.joaodddev.digitalwallet.infrastructure.persistence.entity.TransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    List<TransactionJpaEntity> findAllByWalletIdOrderByCreatedAtDesc(UUID walletId);
}