package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.model.NoteModel;

import java.util.function.Function;

public class NoteToModelFunction implements Function<Note, NoteModel> {

    @Override
    public NoteModel apply(Note note) {

        return NoteModel.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .version(note.getVersion())
                .creationDateTime(note.getCreationDateTime())
                .build();
    }
}
