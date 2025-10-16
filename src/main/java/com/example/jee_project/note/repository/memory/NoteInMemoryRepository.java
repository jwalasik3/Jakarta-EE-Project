package com.example.jee_project.note.repository.memory;

import com.example.jee_project.datastore.component.DataStore;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.repository.api.NoteRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class NoteInMemoryRepository implements NoteRepository {

    private final DataStore store;

    @Inject
    public NoteInMemoryRepository(DataStore store) {

        this.store = store;
    }

    @Override
    public Optional<Note> find(UUID id) {

        return store.findAllNotes().stream()
                .filter(note -> note.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Note> findAll() {

        return store.findAllNotes();
    }

    @Override
    public List<Note> findAll(UUID userId) {

        return store.findAllNotes().stream()
                .filter(note -> note.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Note> findAllByThread(UUID threadId) {

        return store.findAllNotes().stream()
                .filter(note -> note.getNoteThread().getId().equals(threadId))
                .toList();
    }

    @Override
    public void create(Note entity) {

        store.createNote(entity);
    }

    @Override
    public void delete(UUID id) {

        store.deleteNote(id);
    }

    @Override
    public void update(Note entity) {

        store.updateNote(entity);
    }
}
