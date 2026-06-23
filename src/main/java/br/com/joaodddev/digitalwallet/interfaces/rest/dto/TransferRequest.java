package br.com.joaodddev.digitalwallet.interfaces.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(

        @NotNull(message = "senderUserId is required")
        UUID senderUserId,

        @NotNull(message = "receiverUserId is required")
        UUID receiverUserId,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        String description
) {}