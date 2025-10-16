package com.example.jee_project.note.service;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.repository.api.NoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class NoteService {

    private final NoteRepository repository;

    @Inject
    public NoteService(NoteRepository repository) {

        this.repository = repository;
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

    public void createNote(Note note) {

        repository.create(note);
    }

    public void updateNote(Note note) {

        repository.update(note);
    }

    public void deleteNote(UUID id) {

        repository.delete(id);
    }
}
