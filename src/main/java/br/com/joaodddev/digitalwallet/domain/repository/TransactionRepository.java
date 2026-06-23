package br.com.joaodddev.digitalwallet.domain.repository;

import br.com.joaodddev.digitalwallet.domain.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findAllByWalletId(UUID walletId);
}