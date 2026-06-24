package br.com.joaodddev.digitalwallet.domain.service;

import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.exception.InsufficientBalanceException;
import br.com.joaodddev.digitalwallet.domain.exception.SameWalletTransferException;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TransferDomainService")
class TransferDomainServiceTest {

    private TransferDomainService service;

    @BeforeEach
    void setUp() {
        service = new TransferDomainService();
    }

    private Wallet walletWithBalance(String amount) {
        Wallet wallet = Wallet.create(UUID.randomUUID());
        wallet.credit(Money.of(amount));
        return wallet;
    }

    @Nested
    @DisplayName("when valid transfer")
    class WhenValid {

        @Test
        @DisplayName("should debit sender and credit receiver")
        void shouldDebitSenderAndCreditReceiver() {
            Wallet sender = walletWithBalance("500.00");
            Wallet receiver = walletWithBalance("100.00");

            service.execute(sender, receiver, Money.of("200.00"), "Test");

            assertThat(sender.getBalance().amount()).isEqualByComparingTo("300.00");
            assertThat(receiver.getBalance().amount()).isEqualByComparingTo("300.00");
        }

        @Test
        @DisplayName("should return two transactions")
        void shouldReturnTwoTransactions() {
            Wallet sender = walletWithBalance("500.00");
            Wallet receiver = walletWithBalance("0.00");

            List<Transaction> transactions = service.execute(
                    sender, receiver, Money.of("100.00"), "Test");

            assertThat(transactions).hasSize(2);
            assertThat(transactions.get(0).getType()).isEqualTo(TransactionType.TRANSFER_OUT);
            assertThat(transactions.get(1).getType()).isEqualTo(TransactionType.TRANSFER_IN);
        }

        @Test
        @DisplayName("should use default description when null")
        void shouldUseDefaultDescription() {
            Wallet sender = walletWithBalance("500.00");
            Wallet receiver = walletWithBalance("0.00");

            List<Transaction> transactions = service.execute(
                    sender, receiver, Money.of("100.00"), null);

            assertThat(transactions.get(0).getDescription()).contains("Transfer");
        }
    }

    @Nested
    @DisplayName("when invalid transfer")
    class WhenInvalid {

        @Test
        @DisplayName("should throw when sender is the same as receiver")
        void shouldThrowWhenSameWallet() {
            Wallet wallet = walletWithBalance("500.00");
            assertThatThrownBy(() -> service.execute(wallet, wallet, Money.of("100.00"), null))
                    .isInstanceOf(SameWalletTransferException.class);
        }

        @Test
        @DisplayName("should throw when sender has insufficient balance")
        void shouldThrowWhenInsufficientBalance() {
            Wallet sender = walletWithBalance("50.00");
            Wallet receiver = walletWithBalance("0.00");
            assertThatThrownBy(() -> service.execute(sender, receiver, Money.of("100.00"), null))
                    .isInstanceOf(InsufficientBalanceException.class);
        }
    }
}