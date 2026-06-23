package br.com.joaodddev.digitalwallet.application.dto;

public record CreateUserInput(
        String fullName,
        String email,
        String document
) {}