package com.example.jee_project.user.controller.rest;

import com.example.jee_project.component.DtoFunctionFactory;
import com.example.jee_project.user.controller.api.UserController;
import com.example.jee_project.user.dto.GetUserResponse;
import com.example.jee_project.user.dto.GetUsersResponse;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.entity.UserRole;
import com.example.jee_project.user.service.UserService;
import jakarta.annotation.security.PermitAll;
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

import java.io.InputStream;
import java.util.UUID;

@Path("")
public class UserRestController implements UserController {

    private UserService userService;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Inject
    public UserRestController(DtoFunctionFactory factory, @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @EJB
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RolesAllowed(UserRole.ADMIN)
    @Override
    public GetUsersResponse getUsers() {

        return factory.usersToResponseFunction().apply(userService.findAll());
    }

    @RolesAllowed({UserRole.ADMIN, UserRole.USER})
    @Override
    public GetUserResponse getUser(UUID id) {

        return userService.find(id)
                .map(factory.userToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @PermitAll
    @Override
    public void createUser(User user) {

        userService.create(user);
        response.setHeader("Location", uriInfo.getBaseUriBuilder()
                .path(UserController.class, "getUser")
                .build(user.getId())
                .toString());
        throw new WebApplicationException(Response.Status.CREATED);

    }

    @RolesAllowed(UserRole.USER)
    @Override
    public byte[] getUserAvatar(UUID id) {

        byte[] data = userService.findUserAvatar(id);
        if (data == null) {
            throw new NotFoundException();
        }
        return data;
    }

    @RolesAllowed(UserRole.USER)
    @Override
    public void putUserAvatar(UUID id, InputStream avatar) {

        userService.updateAvatar(id, avatar);
        // respond with 204 No Content
        throw new WebApplicationException(Response.Status.NO_CONTENT);
    }

    @RolesAllowed(UserRole.USER)
    @Override
    public void deleteAvatar(UUID id) {

        userService.deleteAvatar(id);
    }
}
