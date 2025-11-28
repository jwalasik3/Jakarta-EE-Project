package com.example.jee_project.note.view;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.logging.annotation.LoggedOperation;
import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.model.NoteEditModel;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.service.NoteService;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class NoteEdit implements Serializable {

    private final ModelFunctionFactory factory;
    private final FacesContext facesContext;
    private NoteService service;
    private NoteThreadService threadService;
    @Setter
    @Getter
    private UUID id;

    @Getter
    private NoteEditModel note;

    @Getter
    private List<ThreadModel> threads;

    private Set<String> changedFields = new HashSet<>();

    @Inject
    public NoteEdit(ModelFunctionFactory factory, FacesContext facesContext) {
        this.facesContext = facesContext;
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

        Optional<Note> note = service.getNoteByCallerPrincipal(id);
        if (note.isPresent()) {
            this.note = factory.noteToEditModel().apply(note.get());
            threads = threadService.getNoteThreads().stream().map(factory.threadToModel()).collect(Collectors.toList());
        } else {
            FacesContext.getCurrentInstance().getExternalContext().responseSendError(HttpServletResponse.SC_NOT_FOUND, "Note not found");
        }
    }

    @LoggedOperation
    public String saveAction() throws IOException {
        try {
            // Build entity from submitted model and update
            service.updateNote(factory.updateNote().apply(
                    service.getNote(id).orElseThrow(), note
            ));

            // successful save -> clear tracked fields
            changedFields.clear();

            String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            return viewId + "?faces-redirect=true&includeViewParams=true";

        } catch (Exception ex) {
            if (ex.getCause() instanceof OptimisticLockException
                    || ex instanceof OptimisticLockException) {

                Note latest = service.getNote(id).orElseThrow();

                if (!changedFields.contains("title")) {
                    note.setTitle(latest.getTitle());
                }
                if (!changedFields.contains("content")) {
                    note.setContent(latest.getContent());
                }

                note.setVersion(latest.getVersion());

                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Version collision",
                        "The note was updated elsewhere. Untouched fields were refreshed."
                ));

                return null;
            }

            throw new RuntimeException(ex);
        }
    }

    public void onFieldChanged(AjaxBehaviorEvent event) {
        UIComponent comp = event.getComponent();
        Object attr = comp.getAttributes().get("fieldName");
        if (attr != null) {
            changedFields.add(attr.toString());
        } else {
            changedFields.add(comp.getId());
        }
    }
}

