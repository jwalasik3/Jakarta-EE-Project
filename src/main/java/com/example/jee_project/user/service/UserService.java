package com.example.jee_project.user.service;

import com.example.jee_project.crypto.component.Pbkdf2PasswordHash;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.repository.api.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class UserService {

    private final UserRepository repository;
    private final Pbkdf2PasswordHash passwordHash;
    private final ServletContext context;
    private final String avatarStore;

    @Inject
    public UserService(UserRepository repository, Pbkdf2PasswordHash passwordHash, ServletContext context) {

        this.repository = repository;
        this.passwordHash = passwordHash;
        this.context = context;
        String avatarStore = this.context.getInitParameter("AVATAR_STORE");
        this.avatarStore = avatarStore.endsWith(File.separator)
                ? avatarStore
                : avatarStore + File.separator;
    }

    public List<User> findAll() {

        return repository.findAll();
    }

    public Optional<User> find(UUID id) {

        return repository.find(id);
    }

    public Optional<User> find(String login) {

        return repository.findByLogin(login);
    }

    @Transactional
    public void create(User user) {

        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    public boolean verify(String login, String password) {

        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

    @Transactional
    public void updateAvatar(UUID id, InputStream is) {

        repository.find(id).ifPresent(user -> {
            Path avatarPath = Paths.get(avatarStore, user.getId() + ".png");
            try {

                if (is == null) {
                    return;
                }
                Files.deleteIfExists(avatarPath);
                Files.createDirectories(avatarPath.getParent());
                try (OutputStream out = Files.newOutputStream(avatarPath, StandardOpenOption.CREATE_NEW)) {
                    is.transferTo(out);
                }
            } catch (IOException ex) {
                throw new IllegalStateException("Failed to update avatar for user " + id, ex);
            }
        });
    }

    @Transactional
    public void deleteAvatar(UUID id) {

        Path avatarPath = Paths.get(avatarStore, id + ".png");

        try {
            if (Files.deleteIfExists(avatarPath)) {
                System.out.println("Deleted avatar for user: " + id);
            } else {
                System.out.println("No avatar found for user: " + id);
            }
        } catch (IOException e) {
            System.err.println("Error deleting avatar for user " + id + ": " + e.getMessage());
            throw new IllegalStateException("Could not delete avatar for user " + id, e);
        }
    }

    public byte[] findUserAvatar(UUID id) {

        Path avatarPath = Paths.get(avatarStore, id + ".png");
        try {
            return Files.exists(avatarPath)
                    ? Files.readAllBytes(avatarPath)
                    : null;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read avatar for user " + id, e);
        }
    }
}
