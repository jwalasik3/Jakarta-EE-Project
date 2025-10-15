package com.example.jee_project.user.service;

import com.example.jee_project.crypto.component.Pbkdf2PasswordHash;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.repository.api.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all business actions regarding user entity.
 */
public class UserService {

    /**
     * Repository for user entity.
     */
    private final UserRepository repository;

    /**
     * Hash mechanism used for storing users' passwords.
     */
    private final Pbkdf2PasswordHash passwordHash;

    private final String avatarStore;

    /**
     * @param repository   repository for character entity
     * @param passwordHash hash mechanism used for storing users' passwords
     */
    public UserService(UserRepository repository, Pbkdf2PasswordHash passwordHash, String avatarStore) {
        this.repository = repository;
        this.passwordHash = passwordHash;
        this.avatarStore = avatarStore;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * @param id user's id
     * @return container (can be empty) with user
     */
    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    /**
     * Seeks for single user using login and password. Can be used in authentication module.
     *
     * @param login user's login
     * @return container (can be empty) with user
     */
    public Optional<User> find(String login) {
        return repository.findByLogin(login);
    }

    /**
     * Saves new user. Password is hashed using configured hash algorithm.
     *
     * @param user new user to be saved
     */
    public void create(User user) {
        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));
        repository.create(user);
    }

    /**
     * @param login    user's login
     * @param password user's password
     * @return true if provided login and password are correct
     */
    public boolean verify(String login, String password) {
        return find(login)
                .map(user -> passwordHash.verify(password.toCharArray(), user.getPassword()))
                .orElse(false);
    }

    public void updateAvatar(UUID id, InputStream is) {
        repository.find(id).ifPresent(user -> {
            try {
                File avatar = new File(avatarStore + user.getId() + ".png");
                if (is == null) {
                    if (avatar.exists()) {
                        avatar.delete();
                    }
                    return;
                }
                if (avatar.exists()) {
                    avatar.delete();
                }
                if (avatar.createNewFile()) {
                    try (OutputStream out = new FileOutputStream(avatar)) {
                        out.write(is.readAllBytes());
                    }
                }

            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }


    public byte[] findUserAvatar(UUID id) throws IOException {

        File avatar = new File(avatarStore + id + ".png");
        if (avatar.exists()) {
            return Files.readAllBytes(avatar.toPath());
        } else {
            return null;
        }
    }
}
