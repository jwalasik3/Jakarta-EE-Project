package com.example.jee_project.user.controller.api;

import com.example.jee_project.user.dto.GetUserResponse;
import com.example.jee_project.user.dto.GetUsersResponse;
import com.example.jee_project.user.entity.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.InputStream;
import java.util.UUID;

@Path("")
public interface UserController {

    @GET
    @Path("/users")
    GetUsersResponse getUsers();

    @GET
    @Path("/users/{id}")
    GetUserResponse getUser(@PathParam("id") UUID id);

    @PUT
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    void createUser(User user);

    @GET
    @Path("/users/{id}/avatar")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    byte[] getUserAvatar(@PathParam("id") UUID id);

    @PUT
    @Path("/users/{id}/avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void putUserAvatar(@PathParam("id") UUID id, InputStream avatar);

    @DELETE
    @Path("/users/{id}/avatar")
    void deleteAvatar(UUID id);
}
