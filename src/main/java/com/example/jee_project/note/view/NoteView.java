package com.example.jee_project.note.view;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.NoteModel;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.service.NoteService;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.ejb.EJB;
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
public class NoteView implements Serializable {

    private NoteService service;
    private final ModelFunctionFactory factory;

    @Setter
    @Getter
    private String id;

    @Getter
    private NoteModel note;

    @Inject
    public NoteView(ModelFunctionFactory factory) {

        this.factory = factory;
    }

    @EJB
    public void setService(NoteService service) {
        this.service = service;
    }

    public void init() throws IOException {

        Optional<Note> note = service.getNote(UUID.fromString(id));
        if (note.isPresent()) {

            this.note = this.factory.noteToModel().apply(note.get());
        } else {

            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
        }
    }
}
