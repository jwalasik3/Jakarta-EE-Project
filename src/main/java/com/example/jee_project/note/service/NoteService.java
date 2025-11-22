package com.example.jee_project.note.service;

import com.example.jee_project.logging.annotation.LoggedOperation;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.repository.api.NoteRepository;
import com.example.jee_project.note.repository.api.NoteThreadRepository;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.entity.UserRole;
import com.example.jee_project.user.repository.api.UserRepository;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@LoggedOperation
@LocalBean
@Stateless
@NoArgsConstructor(force = true)
public class NoteService {

    private final NoteRepository repository;
    private final UserRepository userRepository;
    private final NoteThreadRepository threadRepository;
    private final SecurityContext securityContext;

    @Inject
    public NoteService(NoteRepository repository, UserRepository userRepository,
                       NoteThreadRepository threadRepository, SecurityContext securityContext) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.threadRepository = threadRepository;
        this.securityContext = securityContext;
    }

    public Optional<Note> getNote(UUID id) {

        return repository.find(id);
    }

    @LoggedOperation
    public Optional<Note> getNoteByCallerPrincipal(UUID id) {

        Optional<Note> result = repository.find(id);

        if (result.isEmpty()) {
            return result;
        }

        Note note = result.get();

        if (securityContext.isCallerInRole(UserRole.ADMIN)) {
            return Optional.of(note);
        }

        String username = securityContext.getCallerPrincipal().getName();

        if (note.getUser().getLogin().equals(username)) {
            return Optional.of(note);
        }

        return Optional.empty();
    }

    public List<Note> getAllNotes() {

        return repository.findAll();
    }

    public List<Note> getNotesForCallerPrincipal() {

        if (securityContext.isCallerInRole(UserRole.ADMIN)) {
            return getAllNotes();
        }

        String username = securityContext.getCallerPrincipal().getName();
        return repository.findAllByUsername(username);
    }

    public List<Note> getAllNotesByThread(UUID threadId) {

        return repository.findAllByThread(threadId);
    }

    public List<Note> getAllNotes(UUID userId) {

        return repository.findAll(userId);
    }

    public void createNoteByCallerPrincipal(Note note) {

        String username = securityContext.getCallerPrincipal().getName();
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " does not exist"));
        note.setUser(user);
        createNote(note);
    }

    public void createNote(Note note) {

        if (repository.find(note.getId()).isPresent()) {
            throw new IllegalArgumentException("Note with id " + note.getId() + " already exists");
        }
        if (note.getNoteThread() == null || note.getNoteThread().getId() == null) {
            throw new IllegalArgumentException("NoteThread id must be provided");
        }

        NoteThread managedThread = threadRepository.find(note.getNoteThread().getId())
                .orElseThrow(() -> new IllegalArgumentException("NoteThread with id " + note.getNoteThread().getId() + " does not exist"));

        note.setNoteThread(managedThread);
        managedThread.addNote(note);
        repository.create(note);
    }

    public void updateNote(Note note) {
        Note existing = repository.find(note.getId())
                .orElseThrow(() -> new IllegalArgumentException("Note with id " + note.getId() + " does not exist"));

        checkAdminRoleOrOwner(repository.find(note.getId()));

        UUID oldThreadId = existing.getNoteThread() != null ? existing.getNoteThread().getId() : null;
        UUID newThreadId = note.getNoteThread() != null ? note.getNoteThread().getId() : null;

        if (newThreadId == null) {
            throw new IllegalArgumentException("NoteThread id must be provided");
        }

        if (oldThreadId != null && !oldThreadId.equals(newThreadId)) {
            NoteThread oldThread = threadRepository.find(oldThreadId).orElse(null);
            if (oldThread != null) {
                oldThread.removeNote(existing);
            }

            NoteThread newThread = threadRepository.find(newThreadId)
                    .orElseThrow(() -> new IllegalArgumentException("NoteThread with id " + newThreadId + " does not exist"));
            existing.setNoteThread(newThread);
            newThread.addNote(existing);
        } else {
            NoteThread managedThread = threadRepository.find(newThreadId)
                    .orElseThrow(() -> new IllegalArgumentException("NoteThread with id " + newThreadId + " does not exist"));
            existing.setNoteThread(managedThread);
        }

        existing.setTitle(note.getTitle());
        existing.setContent(note.getContent());
        existing.setUser(note.getUser());
    }

    public void deleteNote(UUID id) {

        Optional<Note> existing = repository.find(id);
        checkAdminRoleOrOwner(existing);
        if (existing.isPresent()) {
            Note note = existing.get();
            if (note.getNoteThread() != null && note.getNoteThread().getId() != null) {
                NoteThread thread = threadRepository.find(note.getNoteThread().getId()).orElse(null);
                if (thread != null) {
                    thread.removeNote(note);
                }
            }
        }
        repository.delete(id);
    }

    private void checkAdminRoleOrOwner(Optional<Note> note) throws EJBAccessException {
        if (securityContext.isCallerInRole(UserRole.ADMIN)) {
            return;
        }
        if (securityContext.isCallerInRole(UserRole.USER)
                && note.isPresent()
                && note.get().getUser().getLogin().equals(securityContext.getCallerPrincipal().getName())) {
            return;
        }
        throw new EJBAccessException("Caller not authorized.");
    }

}
