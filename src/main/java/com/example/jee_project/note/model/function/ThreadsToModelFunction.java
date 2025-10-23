package com.example.jee_project.note.model.function;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadsModel;

import java.util.List;
import java.util.function.Function;

public class ThreadsToModelFunction implements Function<List<NoteThread>, ThreadsModel> {

    @Override
    public ThreadsModel apply(List<NoteThread> noteThreads) {


    }
}
