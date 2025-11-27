package com.example.jee_project.note.model.converter;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.service.NoteThreadService;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.Optional;
import java.util.UUID;

@FacesConverter(value = "noteThreadConverter")
public class NoteThreadConverter implements Converter<ThreadModel> {

    private NoteThreadService threadService;
    private ModelFunctionFactory factory;

    private void init() {
        if (threadService == null || factory == null) {
            threadService = jakarta.enterprise.inject.spi.CDI.current()
                    .select(NoteThreadService.class).get();
            factory = jakarta.enterprise.inject.spi.CDI.current()
                    .select(ModelFunctionFactory.class).get();
        }
    }

    @Override
    public ThreadModel getAsObject(FacesContext context, UIComponent component, String value) {
        init();
        if (value == null || value.isEmpty()) return null;

        try {
            UUID id = UUID.fromString(value);
            Optional<NoteThread> noteThread = threadService.getNoteThread(id);
            return noteThread.map(factory.threadToModel()).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ThreadModel value) {
        if (value == null) return "";
        UUID id = value.getId();
        return id != null ? id.toString() : "";
    }
}
