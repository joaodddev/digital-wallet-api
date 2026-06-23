package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.DepositInput;
import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;
import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.exception.WalletNotFoundException;
import br.com.joaodddev.digitalwallet.domain.repository.TransactionRepository;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public DepositUseCase(WalletRepository walletRepository,
                          TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionOutput execute(DepositInput input) {
        Wallet wallet = walletRepository.findByUserId(input.userId())
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet not found for user: " + input.userId()));

        Money amount = Money.of(input.amount());

        wallet.credit(amount);
        walletRepository.save(wallet);

        Transaction transaction = Transaction.create(
                wallet.getId(),
                amount,
                TransactionType.DEPOSIT,
                input.description() != null ? input.description() : "Deposit"
        );

        return TransactionOutput.from(transactionRepository.save(transaction));
    }
}