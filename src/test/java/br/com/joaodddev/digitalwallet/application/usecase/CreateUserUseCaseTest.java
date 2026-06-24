package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.CreateUserInput;
import br.com.joaodddev.digitalwallet.application.dto.UserOutput;
import br.com.joaodddev.digitalwallet.domain.entity.User;
import br.com.joaodddev.digitalwallet.domain.exception.UserAlreadyExistsException;
import br.com.joaodddev.digitalwallet.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("CreateUserUseCase")
@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCase useCase;

    private CreateUserInput validInput;

    @BeforeEach
    void setUp() {
        validInput = new CreateUserInput("João Silva", "joao@email.com", "12345678909");
    }

    @Nested
    @DisplayName("when input is valid")
    class WhenValid {

        @Test
        @DisplayName("should create and return user")
        void shouldCreateUser() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByDocument(anyString())).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            UserOutput output = useCase.execute(validInput);

            assertThat(output.fullName()).isEqualTo("João Silva");
            assertThat(output.email()).isEqualTo("joao@email.com");
            assertThat(output.id()).isNotNull();
            verify(userRepository).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("when email already exists")
    class WhenEmailExists {

        @Test
        @DisplayName("should throw UserAlreadyExistsException")
        void shouldThrowWhenEmailExists() {
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            assertThatThrownBy(() -> useCase.execute(validInput))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining("Email already registered");

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("when document already exists")
    class WhenDocumentExists {

        @Test
        @DisplayName("should throw UserAlreadyExistsException")
        void shouldThrowWhenDocumentExists() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByDocument(anyString())).thenReturn(true);

            assertThatThrownBy(() -> useCase.execute(validInput))
                    .isInstanceOf(UserAlreadyExistsException.class)
                    .hasMessageContaining("Document already registered");

            verify(userRepository, never()).save(any());
        }
    }
}