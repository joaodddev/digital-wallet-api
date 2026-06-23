package br.com.joaodddev.digitalwallet.domain.repository;

import br.com.joaodddev.digitalwallet.domain.entity.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Wallet save(Wallet wallet);
    Optional<Wallet> findById(UUID id);
    Optional<Wallet> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}