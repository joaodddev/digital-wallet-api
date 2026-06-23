package br.com.joaodddev.digitalwallet.infrastructure.persistence.entity;

import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionStatus;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static TransactionJpaEntity from(Transaction transaction) {
        return new TransactionJpaEntity(
                transaction.getId(),
                transaction.getWalletId(),
                transaction.getAmount().amount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }

    public Transaction toDomain() {
        return Transaction.reconstitute(
                id, walletId, Money.of(amount), type, status, description, createdAt);
    }
}