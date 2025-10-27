package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.NoteCreateModel;
import lombok.SneakyThrows;

import java.util.function.Function;

public class ModelToNoteFunction implements Function<NoteCreateModel, Note> {

    @Override
    @SneakyThrows
    public Note apply(NoteCreateModel noteCreateModel) {

        return Note.builder()
                .id(noteCreateModel.getId())
                .title(noteCreateModel.getTitle())
                .content(noteCreateModel.getContent())
                .noteThread(NoteThread.builder()
                        .id(noteCreateModel.getThread().getId())
                        .build())
                .build();
    }
}
