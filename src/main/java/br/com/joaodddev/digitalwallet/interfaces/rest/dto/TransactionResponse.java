package br.com.joaodddev.digitalwallet.interfaces.rest.dto;

import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID walletId,
        BigDecimal amount,
        String type,
        String status,
        String description,
        LocalDateTime createdAt
) {
    public static TransactionResponse from(TransactionOutput output) {
        return new TransactionResponse(
                output.id(),
                output.walletId(),
                output.amount(),
                output.type(),
                output.status(),
                output.description(),
                output.createdAt()
        );
    }
}