package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadModel;

import java.util.function.Function;
import java.util.stream.Collectors;

public class ThreadToModelFunction implements Function<NoteThread, ThreadModel> {

    @Override
    public ThreadModel apply(NoteThread noteThread) {

        return ThreadModel.builder()
                .id(noteThread.getId())
                .title(noteThread.getTitle())
                .importance(noteThread.getImportance())
                .notes(noteThread.getNotes().stream().map(note -> ThreadModel.Note.builder()
                        .id(note.getId())
                        .title(note.getTitle())
                        .build()).collect(Collectors.toList())
                ).build();
    }
}
