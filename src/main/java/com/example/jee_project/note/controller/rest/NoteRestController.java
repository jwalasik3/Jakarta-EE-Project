package com.example.jee_project.note.controller.rest;

import com.example.jee_project.component.DtoFunctionFactory;
import com.example.jee_project.note.controller.api.NoteController;
import com.example.jee_project.note.dto.GetNoteResponse;
import com.example.jee_project.note.dto.GetNotesResponse;
import com.example.jee_project.note.dto.PatchNoteRequest;
import com.example.jee_project.note.dto.PutNoteRequest;
import com.example.jee_project.note.service.NoteService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@Path("")
public class NoteRestController implements NoteController {

    private final NoteService service;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private final HttpServletResponse response;

    @Inject
    public NoteRestController(NoteService service, DtoFunctionFactory factory,
                              @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo,
                              HttpServletResponse response) {

        this.service = service;
        this.factory = factory;
        this.uriInfo = uriInfo;
        this.response = response;
    }

    @Override
    public GetNotesResponse getNotes() {

        return factory.notesToResponseFunction().apply(service.getAllNotes());
    }

    @Override
    public GetNotesResponse getThreadsNotes(UUID id) {

        return factory.notesToResponseFunction().apply(service.getAllNotesByThread(id));
    }

    @Override
    public GetNoteResponse getNote(UUID id) {

        return service.getNote(id).map(factory.noteToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putNote(UUID threadId, UUID noteId, PutNoteRequest request) {

        service.createNote(factory.requestToNoteFunction().apply(threadId, noteId, request));
        response.setHeader("Location", uriInfo.getBaseUriBuilder()
                .path(NoteController.class, "getNote")
                .build(noteId)
                .toString());
        throw new WebApplicationException(Response.Status.CREATED);
    }

    @Override
    public void patchNote(UUID threadId, UUID noteId, PatchNoteRequest request) {

        service.getNote(noteId).ifPresentOrElse(
                entity -> service.updateNote(factory.updateNote().apply(entity, threadId, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteNote(UUID id) {

        service.getNote(id).ifPresentOrElse(
                entity -> service.deleteNote(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
