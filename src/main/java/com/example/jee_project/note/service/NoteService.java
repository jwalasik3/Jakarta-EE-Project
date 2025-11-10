package com.example.jee_project.note.service;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.repository.api.NoteRepository;
import com.example.jee_project.note.repository.api.NoteThreadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class NoteService {

    private final NoteRepository repository;
    private final NoteThreadRepository threadRepository;

    @Inject
    public NoteService(NoteRepository repository, NoteThreadRepository threadRepository) {

        this.repository = repository;
        this.threadRepository = threadRepository;
    }

    public Optional<Note> getNote(UUID id) {

        return repository.find(id);
    }

    public List<Note> getAllNotes() {

        return repository.findAll();
    }

    public List<Note> getAllNotesByThread(UUID threadId) {

        return repository.findAllByThread(threadId);
    }

    public List<Note> getAllNotes(UUID userId) {

        return repository.findAll(userId);
    }

    @Transactional
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

    @Transactional
    public void updateNote(Note note) {

        Note existing = repository.find(note.getId())
                .orElseThrow(() -> new IllegalArgumentException("Note with id " + note.getId() + " does not exist"));

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

    @Transactional
    public void deleteNote(UUID id) {

        Optional<Note> existing = repository.find(id);
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
}
