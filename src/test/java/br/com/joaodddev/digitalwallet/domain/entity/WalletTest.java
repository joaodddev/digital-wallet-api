package br.com.joaodddev.digitalwallet.domain.entity;

import br.com.joaodddev.digitalwallet.domain.exception.InsufficientBalanceException;
import br.com.joaodddev.digitalwallet.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Wallet Entity")
class WalletTest {

    private Wallet newWallet() {
        return Wallet.create(UUID.randomUUID());
    }

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        @DisplayName("should create wallet with zero balance")
        void shouldCreateWithZeroBalance() {
            Wallet wallet = newWallet();
            assertThat(wallet.getBalance().isZero()).isTrue();
            assertThat(wallet.getId()).isNotNull();
            assertThat(wallet.getCreatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("credit")
    class Credit {

        @Test
        @DisplayName("should increase balance on credit")
        void shouldIncreaseBalance() {
            Wallet wallet = newWallet();
            wallet.credit(Money.of("200.00"));
            assertThat(wallet.getBalance().amount()).isEqualByComparingTo("200.00");
        }

        @Test
        @DisplayName("should accumulate multiple credits")
        void shouldAccumulateCredits() {
            Wallet wallet = newWallet();
            wallet.credit(Money.of("100.00"));
            wallet.credit(Money.of("50.00"));
            assertThat(wallet.getBalance().amount()).isEqualByComparingTo("150.00");
        }
    }

    @Nested
    @DisplayName("debit")
    class Debit {

        @Test
        @DisplayName("should decrease balance on debit")
        void shouldDecreaseBalance() {
            Wallet wallet = newWallet();
            wallet.credit(Money.of("300.00"));
            wallet.debit(Money.of("100.00"));
            assertThat(wallet.getBalance().amount()).isEqualByComparingTo("200.00");
        }

        @Test
        @DisplayName("should throw when insufficient balance")
        void shouldThrowWhenInsufficientBalance() {
            Wallet wallet = newWallet();
            wallet.credit(Money.of("50.00"));
            assertThatThrownBy(() -> wallet.debit(Money.of("100.00")))
                    .isInstanceOf(InsufficientBalanceException.class)
                    .hasMessageContaining("Insufficient balance");
        }

        @Test
        @DisplayName("should allow debit of exact balance")
        void shouldAllowDebitOfExactBalance() {
            Wallet wallet = newWallet();
            wallet.credit(Money.of("100.00"));
            wallet.debit(Money.of("100.00"));
            assertThat(wallet.getBalance().isZero()).isTrue();
        }
    }
}