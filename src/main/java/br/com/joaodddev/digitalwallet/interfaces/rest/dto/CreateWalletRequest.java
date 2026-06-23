package br.com.joaodddev.digitalwallet.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWalletRequest(
        @NotNull(message = "userId is required")
        UUID userId
) {}