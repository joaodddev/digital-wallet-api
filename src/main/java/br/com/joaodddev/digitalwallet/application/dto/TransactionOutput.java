package br.com.joaodddev.digitalwallet.application.dto;

import br.com.joaodddev.digitalwallet.domain.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionOutput(
        UUID id,
        UUID walletId,
        BigDecimal amount,
        String type,
        String status,
        String description,
        LocalDateTime createdAt
) {
    public static TransactionOutput from(Transaction transaction) {
        return new TransactionOutput(
                transaction.getId(),
                transaction.getWalletId(),
                transaction.getAmount().amount(),
                transaction.getType().name(),
                transaction.getStatus().name(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}