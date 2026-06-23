package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;
import br.com.joaodddev.digitalwallet.application.dto.WithdrawalInput;
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
public class WithdrawalUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WithdrawalUseCase(WalletRepository walletRepository,
                             TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionOutput execute(WithdrawalInput input) {
        Wallet wallet = walletRepository.findByUserId(input.userId())
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet not found for user: " + input.userId()));

        Money amount = Money.of(input.amount());

        wallet.debit(amount);
        walletRepository.save(wallet);

        Transaction transaction = Transaction.create(
                wallet.getId(),
                amount,
                TransactionType.WITHDRAWAL,
                input.description() != null ? input.description() : "Withdrawal"
        );

        return TransactionOutput.from(transactionRepository.save(transaction));
    }
}