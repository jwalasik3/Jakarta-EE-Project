package com.example.jee_project.note.view;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.model.ThreadsModel;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class ThreadList {

    private final NoteThreadService service;
    private final ModelFunctionFactory factory;
    private ThreadsModel threads;

    @Inject
    public ThreadList(NoteThreadService service, ModelFunctionFactory factory) {

        this.service = service;
        this.factory = factory;
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
     * @param character character to be removed
     * @return navigation case to list_characters
     */
    public String deleteAction(ThreadsModel.Thread thread) {
        service.delete(thread.g());
        return "character_list?faces-redirect=true";
    }

}
