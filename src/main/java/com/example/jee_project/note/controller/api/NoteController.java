package com.example.jee_project.note.controller.api;

import com.example.jee_project.note.dto.GetNoteResponse;
import com.example.jee_project.note.dto.GetNotesResponse;
import com.example.jee_project.note.dto.PatchNoteRequest;
import com.example.jee_project.note.dto.PutNoteRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

/**
 * Controller for managing collections notes' representations.
 */
@Path("")
public interface NoteController {

    @GET
    @Path("/notes")
    @Produces(MediaType.APPLICATION_JSON)
    GetNotesResponse getNotes();

    @GET
    @Path("/threads/{id}/notes")
    @Produces(MediaType.APPLICATION_JSON)
    GetNotesResponse getThreadsNotes(@PathParam("id") UUID id);

    @GET
    @Path("/notes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetNoteResponse getNote(@PathParam("id") UUID id);

    @PUT
    @Path("/threads/{threadId}/notes/{noteId}")
    @Consumes(MediaType.APPLICATION_JSON)
    void putNote(@PathParam("threadId") UUID threadId, @PathParam("noteId") UUID noteId, PutNoteRequest request);

    @PATCH
    @Path("/threads/{threadId}/notes/{noteId}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchNote(@PathParam("threadId") UUID threadId, @PathParam("noteId") UUID noteId, PatchNoteRequest request);

    @DELETE
    @Path("/notes/{id}")
    void deleteNote(@PathParam("id") UUID id);
}
