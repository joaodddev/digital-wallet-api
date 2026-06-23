package br.com.joaodddev.digitalwallet.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI digitalWalletOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Wallet API")
                        .description("""
                                Fintech-grade Digital Wallet REST API built with Java 21 & Spring Boot 3.
                                
                                ## Features
                                - User registration and management
                                - Wallet creation per user
                                - Deposit and withdrawal operations
                                - Wallet-to-wallet transfers
                                - Transaction history
                                
                                ## Architecture
                                Built following Clean Architecture, DDD, and Hexagonal Architecture principles.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("João")
                                .url("https://github.com/joaodddev"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local development")
                ));
    }
}