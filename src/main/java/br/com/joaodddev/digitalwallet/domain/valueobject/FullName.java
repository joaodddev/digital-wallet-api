package br.com.joaodddev.digitalwallet.domain.valueobject;

import br.com.joaodddev.digitalwallet.domain.exception.InvalidFullNameException;

public record FullName(String value) {

    public FullName {
        if (value == null || value.isBlank()) {
            throw new InvalidFullNameException("Full name must not be blank");
        }
        if (value.trim().length() < 3) {
            throw new InvalidFullNameException("Full name must have at least 3 characters");
        }
        value = value.trim();
    }
}