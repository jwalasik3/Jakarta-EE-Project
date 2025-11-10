package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.GetNoteThreadResponse;
import com.example.jee_project.note.entity.NoteThread;

import java.util.function.Function;

public class NoteThreadToResponseFunction implements Function<NoteThread, GetNoteThreadResponse> {

    @Override
    public GetNoteThreadResponse apply(NoteThread noteThread) {

        return GetNoteThreadResponse.builder()
                .id(noteThread.getId())
                .title(noteThread.getTitle())
                .importance(noteThread.getImportance())
                .build();
    }
}
