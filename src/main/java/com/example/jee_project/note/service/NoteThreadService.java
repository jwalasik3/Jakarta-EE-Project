package com.example.jee_project.note.service;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.repository.api.NoteThreadRepository;
import com.example.jee_project.user.entity.UserRole;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class NoteThreadService {

    NoteThreadRepository repository;
    SecurityContext securityContext;

    @Inject
    public NoteThreadService(NoteThreadRepository repository, SecurityContext securityContext) {

        this.repository = repository;
        this.securityContext = securityContext;
    }

    public Optional<NoteThread> getNoteThread(UUID id) {

        return repository.find(id);
    }

    public Optional<NoteThread> getNoteThreadForCallerPrincipal(UUID id) {

        Optional<NoteThread> result = repository.find(id);

        if (result.isEmpty()) {
            return result;
        }

        NoteThread noteThread = result.get();

        if (securityContext.isCallerInRole(UserRole.ADMIN)) {
            return Optional.of(noteThread);
        }

        String username = securityContext.getCallerPrincipal().getName();

        NoteThread userViewThread = new NoteThread();
        userViewThread.setId(noteThread.getId());
        userViewThread.setTitle(noteThread.getTitle());
        userViewThread.setNotes(
                noteThread.getNotes().stream()
                        .filter(note -> note.getUser().getLogin().equals(username))
                        .toList()
        );

        return Optional.of(userViewThread);
    }


    @RolesAllowed(UserRole.USER)
    public List<NoteThread> getNoteThreads() {

        return repository.findAll();
    }

    public void createNoteThread(NoteThread entity) {

        repository.create(entity);
    }

    public void updateNoteThread(NoteThread entity) {

        repository.update(entity);
    }

    public void deleteNoteThread(UUID id) {

        repository.delete(id);
    }
}
