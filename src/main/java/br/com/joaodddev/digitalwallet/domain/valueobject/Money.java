package br.com.joaodddev.digitalwallet.domain.valueobject;

import br.com.joaodddev.digitalwallet.domain.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        if (amount == null) {
            throw new DomainException("Amount must not be null") {};
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Amount must not be negative") {};
        }
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Money of(BigDecimal value) {
        return new Money(value);
    }

    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return this.amount.compareTo(other.amount) >= 0;
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }
}