package br.com.joaodddev.digitalwallet.application.usecase;

import br.com.joaodddev.digitalwallet.application.dto.CreateUserInput;
import br.com.joaodddev.digitalwallet.application.dto.UserOutput;
import br.com.joaodddev.digitalwallet.domain.entity.User;
import br.com.joaodddev.digitalwallet.domain.exception.UserAlreadyExistsException;
import br.com.joaodddev.digitalwallet.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserOutput execute(CreateUserInput input) {
        if (userRepository.existsByEmail(input.email())) {
            throw new UserAlreadyExistsException("Email already registered: " + input.email());
        }
        if (userRepository.existsByDocument(input.document())) {
            throw new UserAlreadyExistsException("Document already registered: " + input.document());
        }

        User user = User.create(input.fullName(), input.email(), input.document());
        User saved = userRepository.save(user);

        return UserOutput.from(saved);
    }
}