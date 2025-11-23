package com.example.jee_project.note.view;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.model.ThreadsModel;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.UUID;

import com.example.jee_project.logging.annotation.LoggedOperation;

@RequestScoped
@Named
public class ThreadList {

    private NoteThreadService service;
    private final ModelFunctionFactory factory;
    private ThreadsModel threads;

    @Inject
    public ThreadList(ModelFunctionFactory factory) {

        this.factory = factory;
    }

    @EJB
    public void setService(NoteThreadService service) {
        this.service = service;
    }

    public ThreadsModel getThreads() {
        if (threads == null) {
            threads = factory.threadsToModel().apply(service.getNoteThreads());
        }
        return threads;
    }

    /**
     * Action for clicking delete action.
     *
     * @param id ID of thread to be removed
     * @return navigation case to list_characters
     */
    @LoggedOperation
    public String deleteAction(UUID id) {
        service.deleteNoteThread(id);
        return "note_thread_list?faces-redirect=true";
    }

}
