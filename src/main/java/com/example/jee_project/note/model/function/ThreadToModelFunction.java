package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadModel;

import java.util.function.Function;

public class ThreadToModelFunction implements Function<NoteThread, ThreadModel> {

    @Override
    public ThreadModel apply(NoteThread noteThread) {

        return ThreadModel.builder()
                .id(noteThread.getId())
                .title(noteThread.getTitle())
                .build();
    }
}
