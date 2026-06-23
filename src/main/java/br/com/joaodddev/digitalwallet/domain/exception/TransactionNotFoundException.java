package br.com.joaodddev.digitalwallet.domain.exception;

public class TransactionNotFoundException extends DomainException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}