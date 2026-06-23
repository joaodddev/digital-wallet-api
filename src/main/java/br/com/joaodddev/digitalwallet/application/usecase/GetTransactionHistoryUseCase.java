package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;
import br.com.joaodddev.digitalwallet.domain.exception.WalletNotFoundException;
import br.com.joaodddev.digitalwallet.domain.repository.TransactionRepository;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetTransactionHistoryUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public GetTransactionHistoryUseCase(TransactionRepository transactionRepository,
                                        WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public List<TransactionOutput> execute(UUID userId) {
        var wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet not found for user: " + userId));

        return transactionRepository.findAllByWalletId(wallet.getId())
                .stream()
                .map(TransactionOutput::from)
                .toList();
    }
}