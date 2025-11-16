package com.example.jee_project.configuration.singleton;

import com.example.jee_project.note.entity.Importance;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.service.NoteService;
import com.example.jee_project.note.service.NoteThreadService;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.entity.UserRole;
import com.example.jee_project.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
@DependsOn("InitializeAdminService")
@DeclareRoles({UserRole.ADMIN, UserRole.USER})
@RunAs(UserRole.ADMIN)
@NoArgsConstructor
public class InitializedData {

    private UserService userService;
    private NoteService noteService;
    private NoteThreadService noteThreadService;

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @EJB
    public void setNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    @EJB
    public void setNoteThreadService(NoteThreadService noteThreadService) {
        this.noteThreadService = noteThreadService;
    }

    @PostConstruct
    @SneakyThrows
    private void init() {

        if (userService.find("admin").isEmpty()) {
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

            NoteThread thread1 = NoteThread.builder()
                    .id(UUID.fromString("a97c3ee1-d399-4b13-a72a-f24458d590a0"))
                    .title("Side Note")
                    .importance(Importance.LOW)
                    .build();
            NoteThread thread2 = NoteThread.builder()
                    .id(UUID.fromString("a97c3ee1-d399-4b13-a72a-f24458d590a1"))
                    .title("Important Chore Note")
                    .importance(Importance.HIGH)
                    .build();

            noteThreadService.createNoteThread(thread1);
            noteThreadService.createNoteThread(thread2);

            Note note1 = Note.builder()
                    .id(UUID.fromString("a99c3ee1-d399-4b13-a72a-f24458d590a0"))
                    .title("First Note")
                    .user(kevin)
                    .content("This is Kevin's note")
                    .noteThread(thread1)
                    .build();

            Note note2 = Note.builder()
                    .id(UUID.fromString("a99c3ee1-d399-4b13-a72a-f24458d590a1"))
                    .title("Alice's Note")
                    .user(alice)
                    .content("This is Alice's note. Hi, I am Alice.")
                    .noteThread(thread2)
                    .build();

            noteService.createNote(note1);
            noteService.createNote(note2);
        }
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
