package br.com.joaodddev.digitalwallet.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferInput(
        UUID senderUserId,
        UUID receiverUserId,
        BigDecimal amount,
        String description
) {}