package br.com.joaodddev.digitalwallet.domain.exception;

public class WalletAlreadyExistsException extends DomainException {
    public WalletAlreadyExistsException(String message) {
        super(message);
    }
}