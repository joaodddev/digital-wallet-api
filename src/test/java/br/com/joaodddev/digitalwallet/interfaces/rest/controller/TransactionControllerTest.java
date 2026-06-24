package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.DepositInput;
import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;
import br.com.joaodddev.digitalwallet.application.dto.WithdrawalInput;
import br.com.joaodddev.digitalwallet.application.usecase.DepositUseCase;
import br.com.joaodddev.digitalwallet.application.usecase.GetTransactionHistoryUseCase;
import br.com.joaodddev.digitalwallet.application.usecase.WithdrawalUseCase;
import br.com.joaodddev.digitalwallet.domain.exception.InsufficientBalanceException;
import br.com.joaodddev.digitalwallet.domain.exception.WalletNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@DisplayName("TransactionController")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepositUseCase depositUseCase;

    @MockitoBean
    private WithdrawalUseCase withdrawalUseCase;

    @MockitoBean
    private GetTransactionHistoryUseCase getTransactionHistoryUseCase;

    private TransactionOutput sampleOutput(String type) {
        return new TransactionOutput(
                UUID.randomUUID(), UUID.randomUUID(),
                new BigDecimal("100.00"), type, "COMPLETED",
                "Test", LocalDateTime.now());
    }

    @Nested
    @DisplayName("POST /api/v1/transactions/deposit")
    class Deposit {

        @Test
        @DisplayName("should return 201 on successful deposit")
        void shouldReturn201() throws Exception {
            when(depositUseCase.execute(any(DepositInput.class)))
                    .thenReturn(sampleOutput("DEPOSIT"));

            String body = """
                    {
                      "userId": "%s",
                      "amount": 100.00,
                      "description": "Test"
                    }
                    """.formatted(UUID.randomUUID());

            mockMvc.perform(post("/api/v1/transactions/deposit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.type").value("DEPOSIT"))
                    .andExpect(jsonPath("$.status").value("COMPLETED"));
        }

        @Test
        @DisplayName("should return 404 when wallet not found")
        void shouldReturn404() throws Exception {
            when(depositUseCase.execute(any(DepositInput.class)))
                    .thenThrow(new WalletNotFoundException("Wallet not found"));

            String body = """
                    {
                      "userId": "%s",
                      "amount": 100.00
                    }
                    """.formatted(UUID.randomUUID());

            mockMvc.perform(post("/api/v1/transactions/deposit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/transactions/withdrawal")
    class Withdrawal {

        @Test
        @DisplayName("should return 201 on successful withdrawal")
        void shouldReturn201() throws Exception {
            when(withdrawalUseCase.execute(any(WithdrawalInput.class)))
                    .thenReturn(sampleOutput("WITHDRAWAL"));

            String body = """
                    {
                      "userId": "%s",
                      "amount": 100.00
                    }
                    """.formatted(UUID.randomUUID());

            mockMvc.perform(post("/api/v1/transactions/withdrawal")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.type").value("WITHDRAWAL"));
        }

        @Test
        @DisplayName("should return 422 when balance is insufficient")
        void shouldReturn422() throws Exception {
            when(withdrawalUseCase.execute(any(WithdrawalInput.class)))
                    .thenThrow(new InsufficientBalanceException("Insufficient balance"));

            String body = """
                    {
                      "userId": "%s",
                      "amount": 9999.00
                    }
                    """.formatted(UUID.randomUUID());

            mockMvc.perform(post("/api/v1/transactions/withdrawal")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.title").value("Insufficient Balance"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/transactions/users/{userId}")
    class History {

        @Test
        @DisplayName("should return 200 with transaction list")
        void shouldReturn200() throws Exception {
            when(getTransactionHistoryUseCase.execute(any(UUID.class)))
                    .thenReturn(List.of(sampleOutput("DEPOSIT"), sampleOutput("WITHDRAWAL")));

            mockMvc.perform(get("/api/v1/transactions/users/{userId}", UUID.randomUUID()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }
}