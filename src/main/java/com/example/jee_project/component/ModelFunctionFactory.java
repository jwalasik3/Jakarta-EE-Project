package com.example.jee_project.component;

import com.example.jee_project.note.model.function.NoteToModelFunction;
import com.example.jee_project.note.model.function.ThreadToModelFunction;
import com.example.jee_project.note.model.function.ThreadsToModelFunction;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModelFunctionFactory {

    public ThreadToModelFunction threadToModel() {

        return new ThreadToModelFunction();
    }

    public ThreadsToModelFunction threadsToModel() {

        return new ThreadsToModelFunction();
    }

    public NoteToModelFunction noteToModel() {

        return new NoteToModelFunction();
    }
}
