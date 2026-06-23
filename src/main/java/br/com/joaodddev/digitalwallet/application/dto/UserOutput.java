package br.com.joaodddev.digitalwallet.application.dto;

import br.com.joaodddev.digitalwallet.domain.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserOutput(
        UUID id,
        String fullName,
        String email,
        String document,
        String documentType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserOutput from(User user) {
        return new UserOutput(
                user.getId(),
                user.getFullName().value(),
                user.getEmail().value(),
                user.getDocument().value(),
                user.getDocument().type().name(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}