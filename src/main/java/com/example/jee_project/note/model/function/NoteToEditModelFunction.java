package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.model.NoteEditModel;
import com.example.jee_project.note.model.ThreadModel;

import java.util.function.Function;

public class NoteToEditModelFunction implements Function<Note, NoteEditModel> {

    @Override
    public NoteEditModel apply(Note note) {

        return NoteEditModel.builder()
                .title(note.getTitle())
                .content(note.getContent())
                .thread(ThreadModel.builder()
                        .id(note.getNoteThread().getId())
                        .importance(note.getNoteThread().getImportance())
                        .title(note.getNoteThread().getTitle())
                        .build())
                .build();
    }
}
