package com.example.jee_project.note.model.converter;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.util.Optional;
import java.util.UUID;

@FacesConverter(value = "noteThreadConverter", forClass = ThreadModel.class)
public class NoteThreadConverter implements Converter<ThreadModel> {

    private NoteThreadService getService() {
        return CDI.current().select(NoteThreadService.class).get();
    }

    private ModelFunctionFactory getFactory() {
        return CDI.current().select(ModelFunctionFactory.class).get();
    }

    @Override
    public ThreadModel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        Optional<NoteThread> noteThread = getService().getNoteThread(UUID.fromString(value));
        return noteThread.map(getFactory().threadToModel()).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ThreadModel value) {
        return value != null ? value.getId().toString() : "";
    }
}