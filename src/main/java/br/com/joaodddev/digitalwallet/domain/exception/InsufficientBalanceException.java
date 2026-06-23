package br.com.joaodddev.digitalwallet.domain.exception;

public class InsufficientBalanceException extends DomainException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}