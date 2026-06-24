package br.com.joaodddev.digitalwallet.domain.valueobject;

import br.com.joaodddev.digitalwallet.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Money Value Object")
class MoneyTest {

    @Nested
    @DisplayName("creation")
    class Creation {

        @Test
        @DisplayName("should create with scale 2")
        void shouldCreateWithScale2() {
            Money money = Money.of("100");
            assertThat(money.amount()).isEqualByComparingTo("100.00");
            assertThat(money.amount().scale()).isEqualTo(2);
        }

        @Test
        @DisplayName("should throw when null")
        void shouldThrowWhenNull() {
            assertThatThrownBy(() -> new Money(null))
                    .isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("should throw when negative")
        void shouldThrowWhenNegative() {
            assertThatThrownBy(() -> Money.of("-0.01"))
                    .isInstanceOf(DomainException.class);
        }

        @Test
        @DisplayName("ZERO constant should be 0.00")
        void zeroConstant() {
            assertThat(Money.ZERO.amount()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("operations")
    class Operations {

        @Test
        @DisplayName("should add two amounts")
        void shouldAdd() {
            Money result = Money.of("100.00").add(Money.of("50.00"));
            assertThat(result.amount()).isEqualByComparingTo("150.00");
        }

        @Test
        @DisplayName("should subtract two amounts")
        void shouldSubtract() {
            Money result = Money.of("100.00").subtract(Money.of("30.00"));
            assertThat(result.amount()).isEqualByComparingTo("70.00");
        }

        @Test
        @DisplayName("should correctly compare amounts")
        void shouldCompare() {
            assertThat(Money.of("100.00").isGreaterThanOrEqual(Money.of("100.00"))).isTrue();
            assertThat(Money.of("100.00").isGreaterThanOrEqual(Money.of("100.01"))).isFalse();
        }

        @Test
        @DisplayName("should identify zero")
        void shouldIdentifyZero() {
            assertThat(Money.ZERO.isZero()).isTrue();
            assertThat(Money.of("0.01").isZero()).isFalse();
        }
    }
}