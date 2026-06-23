package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.TransferInput;
import br.com.joaodddev.digitalwallet.application.dto.TransferOutput;
import br.com.joaodddev.digitalwallet.application.usecase.TransferUseCase;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.TransferRequest;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.TransferResponse;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/api/v1/transfers")
@Tag(name = "Transfers", description = "Wallet-to-wallet transfers")
public class TransferController {

    private final TransferUseCase transferUseCase;

    public TransferController(TransferUseCase transferUseCase) {
        this.transferUseCase = transferUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Transfer funds between wallets",
            description = """
                    Transfers funds from one user's wallet to another atomically.
                    Generates a TRANSFER_OUT transaction for the sender
                    and a TRANSFER_IN transaction for the receiver.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transfer completed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TransferResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sender or receiver wallet not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "422", description = "Insufficient balance or same-wallet transfer",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TransferResponse> transfer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "senderUserId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                              "receiverUserId": "c9bf9e57-1685-4c89-bafb-ff5af830be8a",
                              "amount": 200.00,
                              "description": "Rent payment"
                            }
                            """)))
            @RequestBody @Valid TransferRequest request) {
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