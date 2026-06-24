package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.DepositInput;
import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;
import br.com.joaodddev.digitalwallet.domain.entity.Transaction;
import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.exception.WalletNotFoundException;
import br.com.joaodddev.digitalwallet.domain.repository.TransactionRepository;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
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

@DisplayName("DepositUseCase")
@ExtendWith(MockitoExtension.class)
class DepositUseCaseTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DepositUseCase useCase;

    @Nested
    @DisplayName("when wallet exists")
    class WhenWalletExists {

        @Test
        @DisplayName("should credit wallet and record DEPOSIT transaction")
        void shouldDepositAndRecordTransaction() {
            UUID userId = UUID.randomUUID();
            Wallet wallet = Wallet.create(userId);

            when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
            when(walletRepository.save(any(Wallet.class))).thenAnswer(inv -> inv.getArgument(0));
            when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

            DepositInput input = new DepositInput(userId, new BigDecimal("300.00"), "Test deposit");
            TransactionOutput output = useCase.execute(input);

            assertThat(output.type()).isEqualTo(TransactionType.DEPOSIT.name());
            assertThat(output.amount()).isEqualByComparingTo("300.00");
            assertThat(wallet.getBalance().amount()).isEqualByComparingTo("300.00");
            verify(walletRepository).save(wallet);
            verify(transactionRepository).save(any(Transaction.class));
        }
    }

    @Nested
    @DisplayName("when wallet not found")
    class WhenWalletNotFound {

        @Test
        @DisplayName("should throw WalletNotFoundException")
        void shouldThrowWhenWalletNotFound() {
            UUID userId = UUID.randomUUID();
            when(walletRepository.findByUserId(userId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> useCase.execute(
                    new DepositInput(userId, new BigDecimal("100.00"), null)))
                    .isInstanceOf(WalletNotFoundException.class);

            verify(transactionRepository, never()).save(any());
        }
    }
}