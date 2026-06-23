package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.TransferInput;
import br.com.joaodddev.digitalwallet.application.dto.TransferOutput;
import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.exception.SameWalletTransferException;
import br.com.joaodddev.digitalwallet.domain.exception.WalletNotFoundException;
import br.com.joaodddev.digitalwallet.domain.repository.TransactionRepository;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
import br.com.joaodddev.digitalwallet.domain.service.TransferDomainService;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferUseCase {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final TransferDomainService transferDomainService;

    public TransferUseCase(WalletRepository walletRepository,
                           TransactionRepository transactionRepository,
                           TransferDomainService transferDomainService) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.transferDomainService = transferDomainService;
    }

    @Transactional
    public TransferOutput execute(TransferInput input) {
        if (input.senderUserId().equals(input.receiverUserId())) {
            throw new SameWalletTransferException("Sender and receiver must be different users");
        }

        Wallet sender = walletRepository.findByUserId(input.senderUserId())
                .orElseThrow(() -> new WalletNotFoundException(
                        "Sender wallet not found for user: " + input.senderUserId()));

        Wallet receiver = walletRepository.findByUserId(input.receiverUserId())
                .orElseThrow(() -> new WalletNotFoundException(
                        "Receiver wallet not found for user: " + input.receiverUserId()));

        Money amount = Money.of(input.amount());

        List<Transaction> transactions = transferDomainService.execute(
                sender, receiver, amount, input.description());

        walletRepository.save(sender);
        walletRepository.save(receiver);

        Transaction out = transactions.get(0);
        Transaction in = transactions.get(1);

        transactionRepository.save(out);
        transactionRepository.save(in);

        return new TransferOutput(
                out.getId(),
                in.getId(),
                sender.getId(),
                receiver.getId(),
                amount.amount(),
                LocalDateTime.now()
        );
    }
}