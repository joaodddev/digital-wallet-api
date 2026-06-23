package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.CreateWalletInput;
import br.com.joaodddev.digitalwallet.application.dto.WalletOutput;
import br.com.joaodddev.digitalwallet.application.usecase.CreateWalletUseCase;
import br.com.joaodddev.digitalwallet.application.usecase.GetWalletUseCase;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.CreateWalletRequest;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.WalletResponse;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallets", description = "Wallet creation and balance inquiry")
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final GetWalletUseCase getWalletUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase,
                            GetWalletUseCase getWalletUseCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.getWalletUseCase = getWalletUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Create a wallet",
            description = "Creates a digital wallet for an existing user. Each user can only have one wallet."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Wallet created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WalletResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Wallet already exists for user",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<WalletResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Wallet creation data",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                            }
                            """)))
            @RequestBody @Valid CreateWalletRequest request) {
        WalletOutput output = createWalletUseCase.execute(new CreateWalletInput(request.userId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(WalletResponse.from(output));
    }

    @GetMapping("/users/{userId}")
    @Operation(
            summary = "Get wallet by user",
            description = "Retrieves the wallet and current balance for the given user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallet found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = WalletResponse.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<WalletResponse> getByUserId(
            @Parameter(description = "User ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable UUID userId) {
        WalletOutput output = getWalletUseCase.execute(userId);
        return ResponseEntity.ok(WalletResponse.from(output));
    }
}