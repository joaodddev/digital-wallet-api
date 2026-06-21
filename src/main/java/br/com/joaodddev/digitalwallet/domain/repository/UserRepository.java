package br.com.joaodddev.digitalwallet.domain.repository;

import br.com.joaodddev.digitalwallet.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByDocument(String document);
    boolean existsByEmail(String email);
    boolean existsByDocument(String document);
}