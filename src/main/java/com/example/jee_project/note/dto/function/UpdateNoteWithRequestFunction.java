package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.PatchNoteRequest;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;

import java.util.UUID;

public class UpdateNoteWithRequestFunction {

    public Note apply(Note entity, UUID threadId, PatchNoteRequest request) {

        return Note.builder()
                .id(entity.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .user(entity.getUser())
                .noteThread(NoteThread.builder().id(threadId).build())
                .build();
    }
}
