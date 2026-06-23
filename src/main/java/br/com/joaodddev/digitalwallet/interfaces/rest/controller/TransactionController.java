package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.DepositInput;
import br.com.joaodddev.digitalwallet.application.dto.TransactionOutput;
import br.com.joaodddev.digitalwallet.application.dto.WithdrawalInput;
import br.com.joaodddev.digitalwallet.application.usecase.DepositUseCase;
import br.com.joaodddev.digitalwallet.application.usecase.GetTransactionHistoryUseCase;
import br.com.joaodddev.digitalwallet.application.usecase.WithdrawalUseCase;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.DepositRequest;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.TransactionResponse;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.WithdrawalRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions", description = "Deposit, withdrawal and transaction history")
public class TransactionController {

    private final DepositUseCase depositUseCase;
    private final WithdrawalUseCase withdrawalUseCase;
    private final GetTransactionHistoryUseCase getTransactionHistoryUseCase;

    public TransactionController(DepositUseCase depositUseCase,
                                 WithdrawalUseCase withdrawalUseCase,
                                 GetTransactionHistoryUseCase getTransactionHistoryUseCase) {
        this.depositUseCase = depositUseCase;
        this.withdrawalUseCase = withdrawalUseCase;
        this.getTransactionHistoryUseCase = getTransactionHistoryUseCase;
    }

    @PostMapping("/deposit")
    @Operation(
            summary = "Deposit funds",
            description = "Credits funds into the user's wallet and records a DEPOSIT transaction."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Deposit completed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "422", description = "Invalid amount",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TransactionResponse> deposit(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                              "amount": 500.00,
                              "description": "Initial deposit"
                            }
                            """)))
            @RequestBody @Valid DepositRequest request) {
        DepositInput input = new DepositInput(request.userId(), request.amount(), request.description());
        TransactionOutput output = depositUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponse.from(output));
    }

    @PostMapping("/withdrawal")
    @Operation(
            summary = "Withdraw funds",
            description = "Debits funds from the user's wallet. Fails if balance is insufficient."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Withdrawal completed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "422", description = "Insufficient balance or invalid amount",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TransactionResponse> withdrawal(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                              "amount": 150.00,
                              "description": "ATM withdrawal"
                            }
                            """)))
            @RequestBody @Valid WithdrawalRequest request) {
        WithdrawalInput input = new WithdrawalInput(request.userId(), request.amount(), request.description());
        TransactionOutput output = withdrawalUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponse.from(output));
    }

    @GetMapping("/users/{userId}")
    @Operation(
            summary = "Get transaction history",
            description = "Returns all transactions for the user's wallet, ordered by most recent first."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction history returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<List<TransactionResponse>> getHistory(
            @Parameter(description = "User ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable UUID userId) {
        List<TransactionResponse> response = getTransactionHistoryUseCase.execute(userId)
                .stream()
                .map(TransactionResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}