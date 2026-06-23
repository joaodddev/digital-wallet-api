package br.com.joaodddev.digitalwallet.interfaces.rest.dto;

import br.com.joaodddev.digitalwallet.application.dto.TransferOutput;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponse(
        UUID transferOutTransactionId,
        UUID transferInTransactionId,
        UUID senderWalletId,
        UUID receiverWalletId,
        BigDecimal amount,
        LocalDateTime executedAt
) {
    public static TransferResponse from(TransferOutput output) {
        return new TransferResponse(
                output.transferOutTransactionId(),
                output.transferInTransactionId(),
                output.senderWalletId(),
                output.receiverWalletId(),
                output.amount(),
                output.executedAt()
        );
    }
}