package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.CreateWalletInput;
import br.com.joaodddev.digitalwallet.application.dto.WalletOutput;
import br.com.joaodddev.digitalwallet.domain.entity.Wallet;
import br.com.joaodddev.digitalwallet.domain.exception.UserNotFoundException;
import br.com.joaodddev.digitalwallet.domain.exception.WalletAlreadyExistsException;
import br.com.joaodddev.digitalwallet.domain.repository.UserRepository;
import br.com.joaodddev.digitalwallet.domain.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateWalletUseCase {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public CreateWalletUseCase(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    public WalletOutput execute(CreateWalletInput input) {
        userRepository.findById(input.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found: " + input.userId()));

        if (walletRepository.existsByUserId(input.userId())) {
            throw new WalletAlreadyExistsException(
                    "Wallet already exists for user: " + input.userId());
        }

        Wallet wallet = Wallet.create(input.userId());
        Wallet saved = walletRepository.save(wallet);

        return WalletOutput.from(saved);
    }
}