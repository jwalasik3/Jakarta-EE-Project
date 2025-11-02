package com.example.jee_project.note.controller.api;

import com.example.jee_project.note.dto.GetNoteThreadResponse;
import com.example.jee_project.note.dto.GetNoteThreadsResponse;
import com.example.jee_project.note.dto.PatchNoteThreadRequest;
import com.example.jee_project.note.dto.PutNoteThreadRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("")
public interface NoteThreadController {

    @GET
    @Path("/threads")
    @Produces(MediaType.APPLICATION_JSON)
    GetNoteThreadsResponse getNoteThreads();

    @GET
    @Path("/threads/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetNoteThreadResponse getNoteThreads(@PathParam("id") UUID id);

    @PUT
    @Path("/threads/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void putNoteThread(@PathParam("id") UUID id, PutNoteThreadRequest request);

    @PATCH
    @Path("/threads/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchNoteThread(@PathParam("id") UUID id, PatchNoteThreadRequest request);

    @DELETE
    @Path("/threads/{id}")
    void deleteNoteThread(@PathParam("id") UUID id);
}
