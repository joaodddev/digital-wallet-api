package br.com.joaodddev.digitalwallet.domain.entity;

import br.com.joaodddev.digitalwallet.domain.exception.InvalidTransactionAmountException;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionStatus;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final UUID walletId;
    private final Money amount;
    private final TransactionType type;
    private final TransactionStatus status;
    private final String description;
    private final LocalDateTime createdAt;

    private Transaction(UUID id, UUID walletId, Money amount, TransactionType type,
                        TransactionStatus status, String description, LocalDateTime createdAt) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static Transaction create(UUID walletId, Money amount,
                                     TransactionType type, String description) {
        if (amount.isZero()) {
            throw new InvalidTransactionAmountException("Transaction amount must be greater than zero");
        }
        return new Transaction(
                UUID.randomUUID(),
                walletId,
                amount,
                type,
                TransactionStatus.COMPLETED,
                description,
                LocalDateTime.now()
        );
    }

    public static Transaction reconstitute(UUID id, UUID walletId, Money amount,
                                           TransactionType type, TransactionStatus status,
                                           String description, LocalDateTime createdAt) {
        return new Transaction(id, walletId, amount, type, status, description, createdAt);
    }

    public UUID getId() { return id; }
    public UUID getWalletId() { return walletId; }
    public Money getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}