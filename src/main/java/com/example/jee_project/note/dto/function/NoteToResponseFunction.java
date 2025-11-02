package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.GetNoteResponse;
import com.example.jee_project.note.entity.Note;

import java.util.function.Function;

public class NoteToResponseFunction implements Function<Note, GetNoteResponse> {

    @Override
    public GetNoteResponse apply(Note note) {

        return GetNoteResponse.builder()
                .id(note.getId())
                .content(note.getContent())
                .build();
    }
}
