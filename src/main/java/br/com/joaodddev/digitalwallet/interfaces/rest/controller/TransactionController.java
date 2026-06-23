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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
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
    public ResponseEntity<TransactionResponse> deposit(@RequestBody @Valid DepositRequest request) {
        DepositInput input = new DepositInput(request.userId(), request.amount(), request.description());
        TransactionOutput output = depositUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponse.from(output));
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<TransactionResponse> withdrawal(@RequestBody @Valid WithdrawalRequest request) {
        WithdrawalInput input = new WithdrawalInput(request.userId(), request.amount(), request.description());
        TransactionOutput output = withdrawalUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponse.from(output));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<TransactionResponse>> getHistory(@PathVariable UUID userId) {
        List<TransactionResponse> response = getTransactionHistoryUseCase.execute(userId)
                .stream()
                .map(TransactionResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}