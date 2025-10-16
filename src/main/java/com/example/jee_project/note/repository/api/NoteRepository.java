package com.example.jee_project.note.repository.api;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.repository.api.Repository;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends Repository<Note, UUID> {

    List<Note> findAll(UUID userId);

    List<Note> findAllByThread(UUID threadId);
}
