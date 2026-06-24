package br.com.joaodddev.digitalwallet.domain.valueobject;

import br.com.joaodddev.digitalwallet.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Email Value Object")
class EmailTest {

    @Nested
    @DisplayName("when valid")
    class WhenValid {

        @Test
        @DisplayName("should create email and normalize to lowercase")
        void shouldCreateAndNormalize() {
            Email email = new Email("JOAO@EMAIL.COM");
            assertThat(email.value()).isEqualTo("joao@email.com");
        }

        @Test
        @DisplayName("should trim whitespace")
        void shouldTrimWhitespace() {
            Email email = new Email("  joao@email.com  ");
            assertThat(email.value()).isEqualTo("joao@email.com");
        }
    }

    @Nested
    @DisplayName("when invalid")
    class WhenInvalid {

        @Test
        @DisplayName("should throw when blank")
        void shouldThrowWhenBlank() {
            assertThatThrownBy(() -> new Email(""))
                    .isInstanceOf(InvalidEmailException.class);
        }

        @Test
        @DisplayName("should throw when null")
        void shouldThrowWhenNull() {
            assertThatThrownBy(() -> new Email(null))
                    .isInstanceOf(InvalidEmailException.class);
        }

        @Test
        @DisplayName("should throw when format is invalid")
        void shouldThrowWhenFormatInvalid() {
            assertThatThrownBy(() -> new Email("not-an-email"))
                    .isInstanceOf(InvalidEmailException.class);
        }
    }
}