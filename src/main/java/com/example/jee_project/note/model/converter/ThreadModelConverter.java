package com.example.jee_project.note.model.converter;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@FacesConverter(forClass = ThreadModel.class, managed = true)
public class ThreadModelConverter implements Converter<ThreadModel> {

    /**
     * Service for professions management.
     */
    private final NoteThreadService service;

    /**
     * Factory producing functions for conversion between models and entities.
     */
    private final ModelFunctionFactory factory;


    /**
     * @param service service for professions management
     * @param factory factory producing functions for conversion between models and entities
     */
    @Inject
    public ThreadModelConverter(NoteThreadService service, ModelFunctionFactory factory) {

        this.service = service;
        this.factory = factory;
    }

    @Override
    public ThreadModel getAsObject(FacesContext context, UIComponent component, String value) {

        if (value == null || value.isBlank()) {
            return null;
        }
        Optional<NoteThread> thread = service.getNoteThread(UUID.fromString(value));
        return thread.map(factory.threadToModel()).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ThreadModel value) {

        return value == null ? "" : value.getId().toString();
    }

}
