package com.example.jee_project.user.controller.simple;

import com.example.jee_project.component.DtoFunctionFactory;
import com.example.jee_project.controller.servlet.exception.BadRequestException;
import com.example.jee_project.controller.servlet.exception.NotFoundException;
import com.example.jee_project.user.controller.api.UserController;
import com.example.jee_project.user.dto.GetUserResponse;
import com.example.jee_project.user.dto.GetUsersResponse;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.service.UserService;

import java.io.InputStream;
import java.util.UUID;

public class UserSimpleController implements UserController {

    private final UserService service;
    private final DtoFunctionFactory dtoFactory;

    public UserSimpleController(UserService service, DtoFunctionFactory dtoFunctionFactory) {

        this.service = service;
        this.dtoFactory = dtoFunctionFactory;
    }

    @Override
    public GetUsersResponse getUsers() {

        return dtoFactory.usersToResponseFunction().apply(service.findAll());
    }

    @Override
    public GetUserResponse getUser(UUID id) {

        return service.find(id).map(dtoFactory.userToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void createUser(User user) {
        try {

            service.create(user);
        } catch (IllegalArgumentException ex) {

            throw new BadRequestException(ex);
        }
    }

    @Override
    public byte[] getUserAvatar(UUID id) {

        return service.find(id)
                .map(User::getAvatar)
                .orElseThrow(NotFoundException::new);

    }

    @Override
    public void putUserAvatar(UUID id, InputStream portrait) {
        service.find(id).ifPresentOrElse(
                entity -> service.updateAvatar(id, portrait),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteAvatar(UUID id) {
        service.find(id).ifPresentOrElse(
                entity -> service.updateAvatar(id, null),
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
