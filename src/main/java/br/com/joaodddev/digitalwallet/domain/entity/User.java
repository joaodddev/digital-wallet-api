package br.com.joaodddev.digitalwallet.domain.entity;

import br.com.joaodddev.digitalwallet.domain.valueobject.Document;
import br.com.joaodddev.digitalwallet.domain.valueobject.Email;
import br.com.joaodddev.digitalwallet.domain.valueobject.FullName;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private final UUID id;
    private FullName fullName;
    private Email email;
    private Document document;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User(UUID id, FullName fullName, Email email, Document document,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.document = document;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String fullName, String email, String document) {
        LocalDateTime now = LocalDateTime.now();
        return new User(
                UUID.randomUUID(),
                new FullName(fullName),
                new Email(email),
                new Document(document),
                now,
                now
        );
    }

    public static User reconstitute(UUID id, String fullName, String email, String document,
                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(
                id,
                new FullName(fullName),
                new Email(email),
                new Document(document),
                createdAt,
                updatedAt
        );
    }

    public void updateFullName(String fullName) {
        this.fullName = new FullName(fullName);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEmail(String email) {
        this.email = new Email(email);
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public FullName getFullName() { return fullName; }
    public Email getEmail() { return email; }
    public Document getDocument() { return document; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}