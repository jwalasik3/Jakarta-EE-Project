package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.GetNotesResponse;
import com.example.jee_project.note.entity.Note;

import java.util.List;
import java.util.function.Function;

public class NotesToResponseFunction implements Function<List<Note>, GetNotesResponse> {

    @Override
    public GetNotesResponse apply(List<Note> notes) {

        return GetNotesResponse.builder()
                .notes(notes.stream().map(note -> GetNotesResponse.Note.builder()
                                .id(note.getId())
                                .title(note.getTitle())
                                .build())
                        .toList())
                .build();
    }
}
