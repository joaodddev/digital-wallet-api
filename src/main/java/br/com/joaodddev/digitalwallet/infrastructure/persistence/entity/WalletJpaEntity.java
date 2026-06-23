package br.com.joaodddev.digitalwallet.infrastructure.persistence.entity;

import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static WalletJpaEntity from(Wallet wallet) {
        return new WalletJpaEntity(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getBalance().amount(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }

    public Wallet toDomain() {
        return Wallet.reconstitute(id, userId, Money.of(balance), createdAt, updatedAt);
    }
}