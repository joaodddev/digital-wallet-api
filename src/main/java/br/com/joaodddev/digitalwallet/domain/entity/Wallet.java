package br.com.joaodddev.digitalwallet.domain.entity;

import br.com.joaodddev.digitalwallet.domain.exception.InsufficientBalanceException;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public class Wallet {

    private final UUID id;
    private final UUID userId;
    private Money balance;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Wallet(UUID id, UUID userId, Money balance,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Wallet create(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return new Wallet(
                UUID.randomUUID(),
                userId,
                Money.ZERO,
                now,
                now
        );
    }

    public static Wallet reconstitute(UUID id, UUID userId, Money balance,
                                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Wallet(id, userId, balance, createdAt, updatedAt);
    }

    public void credit(Money amount) {
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void debit(Money amount) {
        if (!this.balance.isGreaterThanOrEqual(amount)) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Current: " + this.balance.amount()
                            + ", requested: " + amount.amount());
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public Money getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}