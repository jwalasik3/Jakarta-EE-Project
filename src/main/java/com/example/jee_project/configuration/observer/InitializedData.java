package com.example.jee_project.configuration.observer;

import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.entity.UserRole;
import com.example.jee_project.user.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.control.RequestContextController;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class InitializedData {

    private final UserService userService;
    private final RequestContextController requestContextController;

    @Inject
    public InitializedData(
            UserService userService,
            RequestContextController requestContextController
    ) {

        this.userService = userService;
        this.requestContextController = requestContextController;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {

        init();
    }


    /**
     * Initializes database with some example values. Should be called after creating this object. This object should be
     * created only once.
     */
    @SneakyThrows
    private void init() {

        requestContextController.activate();
        User admin = User.builder()
                .id(UUID.fromString("c4804e0f-769e-4ab9-9ebe-0578fb4f00a6"))
                .login("admin")
                .name("System")
                .surname("Admin")
                .birthday(LocalDate.of(1990, 10, 21))
                .email("admin@simple-notes.example.com")
                .password("adminadmin")
                .role(List.of(UserRole.ADMIN, UserRole.USER))
                .build();

        User kevin = User.builder()
                .id(UUID.fromString("81e1c2a9-7f57-439b-b53d-6db88b071e4e"))
                .login("kevin")
                .name("Kevin")
                .surname("Pear")
                .birthday(LocalDate.of(2001, 1, 16))
                .email("kevin@example.com")
                .password("useruser")
                .role(List.of(UserRole.USER))
                .build();

        User alice = User.builder()
                .id(UUID.fromString("ed6cfb2a-cad7-47dd-9b56-9d1e3c7a4197"))
                .login("alice")
                .name("Alice")
                .surname("Grape")
                .birthday(LocalDate.of(2002, 3, 19))
                .email("alice@example.com")
                .password("useruser")
                .role(List.of(UserRole.USER))
                .build();

        User student = User.builder()
                .id(UUID.randomUUID())
                .login("student")
                .name("Student")
                .surname("Studencki")
                .birthday(LocalDate.now())
                .email("student@example.com")
                .password("piwopiwo")
                .role(List.of(UserRole.USER))
                .build();

        userService.create(admin);
        userService.create(kevin);
        userService.create(alice);
        userService.create(student);

        requestContextController.deactivate();

        testNoteAndThreadService();
    }

    private void testNoteAndThreadService() {

        System.out.println("--- Testing Thread Service ---");
        // TODO: Test the thread service here
        System.out.println("--- End of Thread Service Testing ---");

        System.out.println("--- Testing Note Service ---");
        // TODO: Test the note service here
        System.out.println("--- End of Note Service Testing ---");
    }

    /**
     * @param name name of the desired resource
     * @return array of bytes read from the resource
     */
    @SneakyThrows
    private byte[] getResourceAsByteArray(String name) {

        try (InputStream is = this.getClass().getResourceAsStream(name)) {
            if (is != null) {
                return is.readAllBytes();
            } else {
                throw new IllegalStateException("Unable to get resource %s".formatted(name));
            }
        }
    }

}

