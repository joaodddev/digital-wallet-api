package br.com.joaodddev.digitalwallet.interfaces.rest.dto;

import br.com.joaodddev.digitalwallet.application.dto.UserOutput;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        String document,
        String documentType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(UserOutput output) {
        return new UserResponse(
                output.id(),
                output.fullName(),
                output.email(),
                output.document(),
                output.documentType(),
                output.createdAt(),
                output.updatedAt()
        );
    }
}