package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.PutNoteThreadRequest;
import com.example.jee_project.note.entity.NoteThread;

import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToNoteThreadFunction implements BiFunction<UUID, PutNoteThreadRequest, NoteThread> {

    @Override
    public NoteThread apply(UUID id, PutNoteThreadRequest request) {

        return NoteThread.builder()
                .id(id)
                .title(request.getTitle())
                .importance(request.getImportance())
                .build();
    }
}
