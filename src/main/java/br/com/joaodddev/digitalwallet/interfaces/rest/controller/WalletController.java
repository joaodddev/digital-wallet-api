package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.CreateWalletInput;
import br.com.joaodddev.digitalwallet.application.dto.WalletOutput;
import br.com.joaodddev.digitalwallet.application.usecase.CreateWalletUseCase;
import br.com.joaodddev.digitalwallet.application.usecase.GetWalletUseCase;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.CreateWalletRequest;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.WalletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final GetWalletUseCase getWalletUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase,
                            GetWalletUseCase getWalletUseCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.getWalletUseCase = getWalletUseCase;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> create(@RequestBody @Valid CreateWalletRequest request) {
        WalletOutput output = createWalletUseCase.execute(new CreateWalletInput(request.userId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(WalletResponse.from(output));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<WalletResponse> getByUserId(@PathVariable UUID userId) {
        WalletOutput output = getWalletUseCase.execute(userId);
        return ResponseEntity.ok(WalletResponse.from(output));
    }
}