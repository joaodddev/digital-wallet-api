package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.TransferInput;
import br.com.joaodddev.digitalwallet.application.dto.TransferOutput;
import br.com.joaodddev.digitalwallet.application.usecase.TransferUseCase;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.TransferRequest;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferUseCase transferUseCase;

    public TransferController(TransferUseCase transferUseCase) {
        this.transferUseCase = transferUseCase;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@RequestBody @Valid TransferRequest request) {
        TransferInput input = new TransferInput(
                request.senderUserId(),
                request.receiverUserId(),
                request.amount(),
                request.description()
        );
        TransferOutput output = transferUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransferResponse.from(output));
    }
}