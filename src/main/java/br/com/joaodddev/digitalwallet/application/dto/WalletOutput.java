package br.com.joaodddev.digitalwallet.application.dto;

import br.com.joaodddev.digitalwallet.domain.entity.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletOutput(
        UUID id,
        UUID userId,
        BigDecimal balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static WalletOutput from(Wallet wallet) {
        return new WalletOutput(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getBalance().amount(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }
}