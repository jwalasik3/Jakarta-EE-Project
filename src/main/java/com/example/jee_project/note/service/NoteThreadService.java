package com.example.jee_project.note.service;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.repository.api.NoteThreadRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class NoteThreadService {

    NoteThreadRepository repository;

    @Inject
    public NoteThreadService(NoteThreadRepository repository) {

        this.repository = repository;
    }

    public Optional<NoteThread> getNoteThread(UUID id) {

        return repository.find(id);
    }

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
