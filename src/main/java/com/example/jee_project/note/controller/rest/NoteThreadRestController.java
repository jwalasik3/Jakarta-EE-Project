package com.example.jee_project.note.controller.rest;

import com.example.jee_project.component.DtoFunctionFactory;
import com.example.jee_project.note.controller.api.NoteThreadController;
import com.example.jee_project.note.dto.GetNoteThreadResponse;
import com.example.jee_project.note.dto.GetNoteThreadsResponse;
import com.example.jee_project.note.dto.PatchNoteThreadRequest;
import com.example.jee_project.note.dto.PutNoteThreadRequest;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@Path("")
public class NoteThreadRestController implements NoteThreadController {

    private final NoteThreadService service;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private final HttpServletResponse response;

    @Inject
    public NoteThreadRestController(NoteThreadService service, DtoFunctionFactory factory, UriInfo uriInfo, HttpServletResponse response) {

        this.service = service;
        this.factory = factory;
        this.uriInfo = uriInfo;
        this.response = response;
    }

    @Override
    public GetNoteThreadsResponse getNoteThreads() {

        return factory.noteThreadsToResponseFunction().apply(service.getNoteThreads());
    }

    @Override
    public GetNoteThreadResponse getNoteThreads(UUID id) {

        return service.getNoteThread(id)
                .map(factory.noteThreadToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putNoteThread(UUID id, PutNoteThreadRequest request) {

        service.createNoteThread(factory.requestToNoteThreadFunction().apply(id, request));
        response.setHeader("Location", uriInfo.getBaseUriBuilder()
                .path(NoteThreadController.class, "getNoteThread")
                .build(id)
                .toString());
        throw new WebApplicationException(Response.Status.CREATED);

    }

    @Override
    public void patchNoteThread(UUID id, PatchNoteThreadRequest request) {

        service.getNoteThread(id).ifPresentOrElse(
                entity -> service.updateNoteThread(factory.updateNoteThread().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteNoteThread(UUID id) {

        service.getNoteThread(id).ifPresentOrElse(
                entity -> service.deleteNoteThread(id),
                () -> {
                    throw new NotFoundException();
                }
        );

    }
}
