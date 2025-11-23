package com.example.jee_project.note.view;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.logging.annotation.LoggedOperation;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.service.NoteService;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@ViewScoped
@Named
public class ThreadView implements Serializable {

    private final NoteThreadService service;
    private final NoteService noteService;
    private final ModelFunctionFactory factory;

    @Setter
    @Getter
    private String id;

    @Getter
    private ThreadModel noteThread;

    @Inject
    public ThreadView(NoteThreadService service, NoteService noteService, ModelFunctionFactory factory) {

        this.service = service;
        this.noteService = noteService;
        this.factory = factory;
    }

    public void init() throws IOException {

        Optional<NoteThread> noteThread = service.getNoteThreadForCallerPrincipal(UUID.fromString(id));
        if (noteThread.isPresent()) {

            this.noteThread = this.factory.threadToModel().apply(noteThread.get());
        } else {

            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Note thread not found");
        }
    }

    @LoggedOperation
    public String deleteAction(UUID id) {

        noteService.deleteNote(id);
        return String.format("note_thread_view?id=%s&faces-redirect=true", this.id);
    }
}
