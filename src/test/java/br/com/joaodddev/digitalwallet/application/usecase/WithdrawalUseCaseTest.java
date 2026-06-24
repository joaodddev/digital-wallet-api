package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.WithdrawalInput;
import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;
import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.exception.InsufficientBalanceException;
import br.com.joaodddev.digitalwallet.domain.exception.WalletNotFoundException;
import br.com.joaodddev.digitalwallet.domain.repository.TransactionRepository;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import br.com.joaodddev.digitalwallet.domain.valueobject.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("WithdrawalUseCase")
@ExtendWith(MockitoExtension.class)
class WithdrawalUseCaseTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WithdrawalUseCase useCase;

    private Wallet walletWithBalance(UUID userId, String amount) {
        Wallet wallet = Wallet.create(userId);
        wallet.credit(Money.of(amount));
        return wallet;
    }

    @Nested
    @DisplayName("when balance is sufficient")
    class WhenSufficient {

        @Test
        @DisplayName("should debit wallet and record WITHDRAWAL transaction")
        void shouldWithdrawAndRecord() {
            UUID userId = UUID.randomUUID();
            Wallet wallet = walletWithBalance(userId, "500.00");

            when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
            when(walletRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            TransactionOutput output = useCase.execute(
                    new WithdrawalInput(userId, new BigDecimal("200.00"), "Test withdrawal"));

            assertThat(output.type()).isEqualTo(TransactionType.WITHDRAWAL.name());
            assertThat(output.amount()).isEqualByComparingTo("200.00");
            assertThat(wallet.getBalance().amount()).isEqualByComparingTo("300.00");
        }
    }

    @Nested
    @DisplayName("when balance is insufficient")
    class WhenInsufficient {

        @Test
        @DisplayName("should throw InsufficientBalanceException")
        void shouldThrowWhenInsufficient() {
            UUID userId = UUID.randomUUID();
            Wallet wallet = walletWithBalance(userId, "50.00");

            when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));

            assertThatThrownBy(() -> useCase.execute(
                    new WithdrawalInput(userId, new BigDecimal("200.00"), null)))
                    .isInstanceOf(InsufficientBalanceException.class);

            verify(transactionRepository, never()).save(any(Transaction.class));
        }
    }

    @Nested
    @DisplayName("when wallet not found")
    class WhenNotFound {

        @Test
        @DisplayName("should throw WalletNotFoundException")
        void shouldThrow() {
            UUID userId = UUID.randomUUID();
            when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(
                    new WithdrawalInput(userId, new BigDecimal("100.00"), null)))
                    .isInstanceOf(WalletNotFoundException.class);
        }
    }
}