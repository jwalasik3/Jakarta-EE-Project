package com.example.jee_project.component;

import com.example.jee_project.note.model.function.*;
import com.example.jee_project.user.model.function.UserToModelFunction;
import com.example.jee_project.user.model.function.UsersToModelFunction;
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

    public ModelToNoteFunction modelToNote() {

        return new ModelToNoteFunction();
    }

    public NoteToEditModelFunction noteToEditModel() {

        return new NoteToEditModelFunction();
    }

    public UpdateNoteWithModelFunction updateNote() {

        return new UpdateNoteWithModelFunction();
    }

    public UserToModelFunction userToModel() {
        return new UserToModelFunction();
    }

    public UsersToModelFunction usersToModel() {
        return new UsersToModelFunction();
    }

}
