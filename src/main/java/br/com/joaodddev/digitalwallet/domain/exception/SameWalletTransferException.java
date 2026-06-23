package br.com.joaodddev.digitalwallet.domain.exception;

public class SameWalletTransferException extends DomainException {
    public SameWalletTransferException(String message) {
        super(message);
    }
}