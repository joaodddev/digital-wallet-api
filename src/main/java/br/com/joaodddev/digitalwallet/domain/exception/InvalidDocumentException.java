package br.com.joaodddev.digitalwallet.domain.exception;

public class InvalidDocumentException extends DomainException {
    public InvalidDocumentException(String message) {
        super(message);
    }
}