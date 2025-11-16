package com.example.jee_project.note.controller.rest;

import com.example.jee_project.component.DtoFunctionFactory;
import com.example.jee_project.note.controller.api.NoteThreadController;
import com.example.jee_project.note.dto.GetNoteThreadResponse;
import com.example.jee_project.note.dto.GetNoteThreadsResponse;
import com.example.jee_project.note.dto.PatchNoteThreadRequest;
import com.example.jee_project.note.dto.PutNoteThreadRequest;
import com.example.jee_project.note.service.NoteThreadService;
import com.example.jee_project.user.entity.UserRole;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.UUID;

@Path("")
public class NoteThreadRestController implements NoteThreadController {

    private NoteThreadService service;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Inject
    public NoteThreadRestController(DtoFunctionFactory factory, UriInfo uriInfo) {

        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @EJB
    public void setService(NoteThreadService service) {
        this.service = service;
    }

    @RolesAllowed(UserRole.USER)
    @Override
    public GetNoteThreadsResponse getNoteThreads() {

        return factory.noteThreadsToResponseFunction().apply(service.getNoteThreads());
    }

    @RolesAllowed(UserRole.USER)
    @Override
    public GetNoteThreadResponse getNoteThread(UUID id) {

        return service.getNoteThread(id)
                .map(factory.noteThreadToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @RolesAllowed(UserRole.ADMIN)
    @Override
    public void putNoteThread(UUID id, PutNoteThreadRequest request) {

        service.createNoteThread(factory.requestToNoteThreadFunction().apply(id, request));
        response.setHeader("Location", uriInfo.getBaseUriBuilder()
                .path(NoteThreadController.class, "getNoteThread")
                .build(id)
                .toString());
        throw new WebApplicationException(Response.Status.CREATED);

    }

    @RolesAllowed(UserRole.ADMIN)
    @Override
    public void patchNoteThread(UUID id, PatchNoteThreadRequest request) {

        service.getNoteThread(id).ifPresentOrElse(
                entity -> service.updateNoteThread(factory.updateNoteThread().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @RolesAllowed(UserRole.ADMIN)
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
