package br.com.joaodddev.digitalwallet.domain.service;

import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.exception.SameWalletTransferException;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionType;

import java.util.List;

public class TransferDomainService {

    public List<Transaction> execute(Wallet sender, Wallet receiver, Money amount, String description) {
        if (sender.getId().equals(receiver.getId())) {
            throw new SameWalletTransferException("Cannot transfer to the same wallet");
        }

        sender.debit(amount);
        receiver.credit(amount);

        String desc = description != null ? description : "Transfer";

        Transaction out = Transaction.create(
                sender.getId(), amount, TransactionType.TRANSFER_OUT,
                desc + " → " + receiver.getId());

        Transaction in = Transaction.create(
                receiver.getId(), amount, TransactionType.TRANSFER_IN,
                desc + " ← " + sender.getId());

        return List.of(out, in);
    }
}