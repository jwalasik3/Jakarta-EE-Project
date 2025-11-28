package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.model.NoteEditModel;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

public class UpdateNoteWithModelFunction implements BiFunction<Note, NoteEditModel, Note> {

    @Override
    @SneakyThrows
    public Note apply(Note entity, NoteEditModel request) {

        return Note.builder()
                .id(entity.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .noteThread(entity.getNoteThread())
                .version(request.getVersion())
                .creationDateTime(entity.getCreationDateTime())
                .modificationDateTime(LocalDateTime.now())
                .user(entity.getUser())
                .build();
    }
}
