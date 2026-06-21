package br.com.joaodddev.digitalwallet.domain.valueobject;

import br.com.joaodddev.digitalwallet.domain.exception.InvalidDocumentException;

public record Document(String value) {

    public Document {
        if (value == null || value.isBlank()) {
            throw new InvalidDocumentException("Document must not be blank");
        }
        value = value.replaceAll("[.\\-/]", "").trim();
        if (!isValidCpf(value) && !isValidCnpj(value)) {
            throw new InvalidDocumentException("Invalid CPF or CNPJ: " + value);
        }
    }

    public DocumentType type() {
        return value.length() == 11 ? DocumentType.CPF : DocumentType.CNPJ;
    }

    private static boolean isValidCpf(String value) {
        return value.matches("\\d{11}");
    }

    private static boolean isValidCnpj(String value) {
        return value.matches("\\d{14}");
    }

    public enum DocumentType {
        CPF, CNPJ
    }
}