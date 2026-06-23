package br.com.joaodddev.digitalwallet.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferOutput(
        UUID transferOutTransactionId,
        UUID transferInTransactionId,
        UUID senderWalletId,
        UUID receiverWalletId,
        BigDecimal amount,
        LocalDateTime executedAt
) {}