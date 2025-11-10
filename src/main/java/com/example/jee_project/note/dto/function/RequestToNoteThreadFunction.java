package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.PutNoteThreadRequest;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class RequestToNoteThreadFunction implements BiFunction<UUID, PutNoteThreadRequest, NoteThread> {

    @Override
    public NoteThread apply(UUID id, PutNoteThreadRequest request) {

        return NoteThread.builder()
                .id(id)
                .title(request.getTitle())
                .importance(request.getImportance())
                .notes(request.getNotes() == null ? new ArrayList<Note>() :
                        request.getNotes().stream().map(noteId -> Note.builder()
                                .id(noteId)
                                .build()).collect(Collectors.toList()))
                .build();
    }
}
