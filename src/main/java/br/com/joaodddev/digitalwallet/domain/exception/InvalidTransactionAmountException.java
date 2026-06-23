package br.com.joaodddev.digitalwallet.domain.exception;

public class InvalidTransactionAmountException extends DomainException {
    public InvalidTransactionAmountException(String message) {
        super(message);
    }
}