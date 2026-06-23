package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.CreateUserInput;
import br.com.joaodddev.digitalwallet.application.dto.UserOutput;
import br.com.joaodddev.digitalwallet.application.usecase.CreateUserUseCase;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.CreateUserRequest;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        CreateUserInput input = new CreateUserInput(
                request.fullName(),
                request.email(),
                request.document()
        );
        UserOutput output = createUserUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(output));
    }
}