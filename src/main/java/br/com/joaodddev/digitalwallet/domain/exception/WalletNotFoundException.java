package br.com.joaodddev.digitalwallet.domain.exception;

public class WalletNotFoundException extends DomainException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}