package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.CreateUserInput;
import br.com.joaodddev.digitalwallet.application.dto.UserOutput;
import br.com.joaodddev.digitalwallet.application.usecase.CreateUserUseCase;
import br.com.joaodddev.digitalwallet.domain.exception.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    private static final String URL = "/api/v1/users";

    @Nested
    @DisplayName("POST /api/v1/users")
    class Create {

        @Test
        @DisplayName("should return 201 when user is created successfully")
        void shouldReturn201() throws Exception {
            UserOutput output = new UserOutput(
                    UUID.randomUUID(), "João Silva", "joao@email.com",
                    "12345678909", "CPF", LocalDateTime.now(), LocalDateTime.now());

            when(createUserUseCase.execute(any(CreateUserInput.class))).thenReturn(output);

            String body = """
                    {
                      "fullName": "João Silva",
                      "email": "joao@email.com",
                      "document": "12345678909"
                    }
                    """;

            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.email").value("joao@email.com"))
                    .andExpect(jsonPath("$.documentType").value("CPF"));
        }

        @Test
        @DisplayName("should return 409 when email is already registered")
        void shouldReturn409WhenDuplicate() throws Exception {
            when(createUserUseCase.execute(any(CreateUserInput.class)))
                    .thenThrow(new UserAlreadyExistsException("Email already registered"));

            String body = """
                    {
                      "fullName": "João Silva",
                      "email": "joao@email.com",
                      "document": "12345678909"
                    }
                    """;

            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.title").value("User Already Exists"));
        }

        @Test
        @DisplayName("should return 400 when required fields are missing")
        void shouldReturn400WhenInvalidBody() throws Exception {
            String body = """
                    {
                      "email": "joao@email.com"
                    }
                    """;

            mockMvc.perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("Validation Error"));
        }
    }
}