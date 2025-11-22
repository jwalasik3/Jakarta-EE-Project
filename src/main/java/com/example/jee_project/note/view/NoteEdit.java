package com.example.jee_project.note.view;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.NoteEditModel;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class NoteEdit implements Serializable {

    private NoteService service;
    private NoteThreadService threadService;
    private final ModelFunctionFactory factory;

    @Setter
    @Getter
    private UUID id;

    @Getter
    private NoteEditModel note;

    @Getter
    private List<ThreadModel> threads;

    @Inject
    public NoteEdit(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    @EJB
    public void setService(NoteService service) {
        this.service = service;
    }

    @EJB
    public void setThreadService(NoteThreadService threadService) {
        this.threadService = threadService;
    }

    public void init() throws IOException {
        System.out.println("Wywołano init(), id: " + id);
        Optional<Note> note = service.getNoteByCallerPrincipal(id);
        if (note.isPresent()) {
            this.note = factory.noteToEditModel().apply(note.get());
            threads = threadService.getNoteThreads().stream().map(factory.threadToModel()).collect(Collectors.toList());
            System.out.println("Liczba wątków: " + threads.size());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
        }
    }

    public String saveAction() {
        service.updateNote(factory.updateNote().apply(service.getNote(id).orElseThrow(), note));
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&includeViewParams=true";
    }

}
