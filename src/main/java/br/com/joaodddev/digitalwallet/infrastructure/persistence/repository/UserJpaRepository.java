package br.com.joaodddev.digitalwallet.infrastructure.persistence.repository;

import br.com.joaodddev.digitalwallet.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByDocument(String document);
    boolean existsByEmail(String email);
    boolean existsByDocument(String document);
}