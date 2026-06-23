package br.com.joaodddev.digitalwallet.interfaces.rest.dto;

import br.com.joaodddev.digitalwallet.application.dto.WalletOutput;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WalletResponse(
        UUID id,
        UUID userId,
        BigDecimal balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static WalletResponse from(WalletOutput output) {
        return new WalletResponse(
                output.id(),
                output.userId(),
                output.balance(),
                output.createdAt(),
                output.updatedAt()
        );
    }
}