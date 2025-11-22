package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.NoteEditModel;
import lombok.SneakyThrows;

import java.util.function.BiFunction;

public class UpdateNoteWithModelFunction implements BiFunction<Note, NoteEditModel, Note> {

    @Override
    @SneakyThrows
    public Note apply(Note entity, NoteEditModel request) {

        return Note.builder()
                .id(entity.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .noteThread(NoteThread.builder()
                        .id(request.getThread().getId())
                        .title(request.getThread().getTitle())
                        .importance(request.getThread().getImportance())
                        .build())
                .user(entity.getUser())
                .build();
    }
}
