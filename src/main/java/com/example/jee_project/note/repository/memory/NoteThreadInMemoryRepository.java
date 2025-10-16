package com.example.jee_project.note.repository.memory;

import com.example.jee_project.datastore.component.DataStore;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.repository.api.NoteThreadRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class NoteThreadInMemoryRepository implements NoteThreadRepository {

    private final DataStore store;

    @Inject
    public NoteThreadInMemoryRepository(DataStore store) {

        this.store = store;
    }

    @Override
    public Optional<NoteThread> find(UUID id) {

        return store.findAllNoteThreads().stream()
                .filter(thread -> thread.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<NoteThread> findAll() {

        return store.findAllNoteThreads();
    }

    @Override
    public void create(NoteThread entity) {

        store.createNoteThread(entity);
    }

    @Override
    public void delete(UUID id) {

        throw new UnsupportedOperationException("Operation not implemented.");
    }

    @Override
    public void update(NoteThread entity) {

        throw new UnsupportedOperationException("Operation not implemented.");
    }
}
