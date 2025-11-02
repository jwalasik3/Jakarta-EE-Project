package com.example.jee_project.note.dto.function;


import com.example.jee_project.note.dto.PutNoteRequest;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;

import java.util.UUID;

public class RequestToNoteFunction {

    public Note apply(UUID threadId, UUID noteId, PutNoteRequest request) {

        return Note.builder()
                .id(noteId)
                .title(request.getTitle())
                .content(request.getContent())
                .noteThread(NoteThread.builder().id(threadId).build())
                .build();
    }
}
