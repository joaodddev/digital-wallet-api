package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.WalletOutput;
import br.com.joaodddev.digitalwallet.domain.exception.WalletNotFoundException;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetWalletUseCase {

    private final WalletRepository walletRepository;

    public GetWalletUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletOutput execute(UUID userId) {
        return walletRepository.findByUserId(userId)
                .map(WalletOutput::from)
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet not found for user: " + userId));
    }
}