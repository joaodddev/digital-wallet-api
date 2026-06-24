package br.com.joaodddev.digitalwallet.domain.valueobject;

import br.com.joaodddev.digitalwallet.domain.exception.InvalidDocumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Document Value Object")
class DocumentTest {

    @Nested
    @DisplayName("when CPF")
    class WhenCpf {

        @Test
        @DisplayName("should accept formatted CPF and strip punctuation")
        void shouldAcceptFormattedCpf() {
            Document doc = new Document("123.456.789-09");
            assertThat(doc.value()).isEqualTo("12345678909");
            assertThat(doc.type()).isEqualTo(Document.DocumentType.CPF);
        }

        @Test
        @DisplayName("should accept raw CPF digits")
        void shouldAcceptRawCpf() {
            Document doc = new Document("12345678909");
            assertThat(doc.value()).isEqualTo("12345678909");
        }
    }

    @Nested
    @DisplayName("when CNPJ")
    class WhenCnpj {

        @Test
        @DisplayName("should accept formatted CNPJ and strip punctuation")
        void shouldAcceptFormattedCnpj() {
            Document doc = new Document("11.222.333/0001-81");
            assertThat(doc.value()).isEqualTo("11222333000181");
            assertThat(doc.type()).isEqualTo(Document.DocumentType.CNPJ);
        }
    }

    @Nested
    @DisplayName("when invalid")
    class WhenInvalid {

        @Test
        @DisplayName("should throw when blank")
        void shouldThrowWhenBlank() {
            assertThatThrownBy(() -> new Document(""))
                    .isInstanceOf(InvalidDocumentException.class);
        }

        @Test
        @DisplayName("should throw when wrong length")
        void shouldThrowWhenWrongLength() {
            assertThatThrownBy(() -> new Document("12345"))
                    .isInstanceOf(InvalidDocumentException.class);
        }
    }
}