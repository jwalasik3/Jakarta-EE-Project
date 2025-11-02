package com.example.jee_project.component;

import com.example.jee_project.note.dto.function.*;
import com.example.jee_project.user.dto.function.UserToResponseFunction;
import com.example.jee_project.user.dto.function.UsersToResponseFunction;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DtoFunctionFactory {

    public UserToResponseFunction userToResponseFunction() {

        return new UserToResponseFunction();
    }

    public UsersToResponseFunction usersToResponseFunction() {

        return new UsersToResponseFunction();
    }

    public NoteToResponseFunction noteToResponseFunction() {

        return new NoteToResponseFunction();
    }

    public NotesToResponseFunction notesToResponseFunction() {

        return new NotesToResponseFunction();
    }

    public NoteThreadToResponseFunction noteThreadToResponseFunction() {

        return new NoteThreadToResponseFunction();
    }

    public NoteThreadsToResponseFunction noteThreadsToResponseFunction() {

        return new NoteThreadsToResponseFunction();
    }

    public RequestToNoteFunction requestToNoteFunction() {

        return new RequestToNoteFunction();
    }

    public RequestToNoteThreadFunction requestToNoteThreadFunction() {

        return new RequestToNoteThreadFunction();
    }

    public UpdateNoteThreadWithRequestFunction updateNoteThread() {

        return new UpdateNoteThreadWithRequestFunction();
    }

    public UpdateNoteWithRequestFunction updateNote() {

        return new UpdateNoteWithRequestFunction();
    }
}
