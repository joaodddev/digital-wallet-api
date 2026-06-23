package br.com.joaodddev.digitalwallet.infrastructure.persistence.repository;

import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.repository.TransactionRepository;
import br.com.joaodddev.digitalwallet.infrastructure.persistence.entity.TransactionJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryAdapter(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return jpaRepository.save(TransactionJpaEntity.from(transaction)).toDomain();
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpaRepository.findById(id).map(TransactionJpaEntity::toDomain);
    }

    @Override
    public List<Transaction> findAllByWalletId(UUID walletId) {
        return jpaRepository.findAllByWalletIdOrderByCreatedAtDesc(walletId)
                .stream()
                .map(TransactionJpaEntity::toDomain)
                .toList();
    }
}