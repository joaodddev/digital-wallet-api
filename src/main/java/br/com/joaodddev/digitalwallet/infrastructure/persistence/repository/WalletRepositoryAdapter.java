package br.com.joaodddev.digitalwallet.infrastructure.persistence.repository;

import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
import br.com.joaodddev.digitalwallet.infrastructure.persistence.entity.WalletJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class WalletRepositoryAdapter implements WalletRepository {

    private final WalletJpaRepository jpaRepository;

    public WalletRepositoryAdapter(WalletJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Wallet save(Wallet wallet) {
        return jpaRepository.save(WalletJpaEntity.from(wallet)).toDomain();
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        return jpaRepository.findById(id).map(WalletJpaEntity::toDomain);
    }

    @Override
    public Optional<Wallet> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(WalletJpaEntity::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }
}