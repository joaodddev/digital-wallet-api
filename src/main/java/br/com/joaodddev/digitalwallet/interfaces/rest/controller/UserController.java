package br.com.joaodddev.digitalwallet.interfaces.rest.controller;

import br.com.joaodddev.digitalwallet.application.dto.CreateUserInput;
import br.com.joaodddev.digitalwallet.application.dto.UserOutput;
import br.com.joaodddev.digitalwallet.application.usecase.CreateUserUseCase;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.CreateUserRequest;
import br.com.joaodddev.digitalwallet.interfaces.rest.dto.UserResponse;
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
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User registration and management")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account. Email and document (CPF/CNPJ) must be unique."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Email or document already registered",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "422", description = "Domain validation error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<UserResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                              "fullName": "João Silva",
                              "email": "joao@email.com",
                              "document": "123.456.789-09"
                            }
                            """)))
            @RequestBody @Valid CreateUserRequest request) {
        CreateUserInput input = new CreateUserInput(
                request.fullName(), request.email(), request.document());
        UserOutput output = createUserUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(output));
    }
}