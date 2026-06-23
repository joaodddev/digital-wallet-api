package br.com.joaodddev.digitalwallet.infrastructure.config;

import br.com.joaodddev.digitalwallet.domain.service.TransferDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public TransferDomainService transferDomainService() {
        return new TransferDomainService();
    }
}