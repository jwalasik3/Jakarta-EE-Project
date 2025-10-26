package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadsModel;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ThreadsToModelFunction implements Function<List<NoteThread>, ThreadsModel> {

    @Override
    public ThreadsModel apply(List<NoteThread> noteThreads) {

        return ThreadsModel.builder().threads(
                noteThreads.stream().map(noteThread ->
                        ThreadsModel.Thread.builder()
                                .id(noteThread.getId())
                                .title(noteThread.getTitle())
                                .build()).collect(Collectors.toList())
        ).build();
    }
}
