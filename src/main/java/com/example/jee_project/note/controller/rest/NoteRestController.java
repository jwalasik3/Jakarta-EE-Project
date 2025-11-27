package com.example.jee_project.note.controller.rest;

import com.example.jee_project.component.DtoFunctionFactory;
import com.example.jee_project.note.controller.api.NoteController;
import com.example.jee_project.note.dto.GetNoteResponse;
import com.example.jee_project.note.dto.GetNotesResponse;
import com.example.jee_project.note.dto.PatchNoteRequest;
import com.example.jee_project.note.dto.PutNoteRequest;
import com.example.jee_project.note.service.NoteService;
import com.example.jee_project.user.entity.UserRole;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.TransactionalException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.java.Log;

import java.util.UUID;
import java.util.logging.Level;

@Path("")
@Log
@RolesAllowed(UserRole.USER)
public class NoteRestController implements NoteController {

    private NoteService service;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Inject
    public NoteRestController(DtoFunctionFactory factory, @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {

        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @EJB
    public void setService(NoteService service) {
        this.service = service;
    }

    @RolesAllowed({ UserRole.USER, UserRole.ADMIN })
    @Override
    public GetNotesResponse getNotes() {

        return factory.notesToResponseFunction().apply(service.getNotesForCallerPrincipal());
    }

    @RolesAllowed(UserRole.ADMIN)
    @Override
    public GetNotesResponse getThreadsNotes(UUID id) {

        return factory.notesToResponseFunction().apply(service.getAllNotesByThread(id));
    }

    @RolesAllowed(UserRole.USER)
    @Override
    public GetNoteResponse getNote(UUID id) {

        return service.getNote(id).map(factory.noteToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @RolesAllowed({ UserRole.USER, UserRole.ADMIN })
    @Override
    public void putNote(UUID threadId, UUID noteId, PutNoteRequest request) {

        service.createNoteByCallerPrincipal(factory.requestToNoteFunction().apply(threadId, noteId, request));
        response.setHeader("Location", uriInfo.getBaseUriBuilder()
                .path(NoteController.class, "getNote")
                .build(noteId)
                .toString());
        throw new WebApplicationException(Response.Status.CREATED);
    }

    @RolesAllowed({ UserRole.USER, UserRole.ADMIN })
    @Override
    public void patchNote(UUID threadId, UUID noteId, PatchNoteRequest request) {

        try {

            service.getNote(noteId).ifPresentOrElse(
                    entity -> {
                        try {
                            service.updateNote(factory.updateNote().apply(entity, threadId, request));
                        } catch (EJBAccessException e) {
                            log.log(Level.WARNING, e.getMessage(), e);
                            throw new ForbiddenException();
                        }
                    },
                    () -> {
                        throw new NotFoundException();
                    }
            );
        } catch (TransactionalException ex) {

            if (ex.getCause() instanceof OptimisticLockException) {
                throw new BadRequestException(ex.getCause());
            }
        }
    }

    @RolesAllowed({ UserRole.USER, UserRole.ADMIN })
    @Override
    public void deleteNote(UUID id) {

        service.getNote(id).ifPresentOrElse(
                entity -> {
                    try {
                        service.deleteNote(id);
                    } catch (EJBAccessException e) {
                        log.log(Level.WARNING, e.getMessage(), e);
                        throw new ForbiddenException();
                    }
                },
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
