package com.example.jee_project.user.controller.api;

import com.example.jee_project.user.dto.GetUserResponse;
import com.example.jee_project.user.dto.GetUsersResponse;
import com.example.jee_project.user.entity.User;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface UserController {

    GetUsersResponse getUsers();
    GetUserResponse getUser(UUID id);
    void createUser(User user);

    byte[] getUserAvatar(UUID id);
    void putUserAvatar(UUID id, InputStream avatar);
    void deleteAvatar(UUID id);
}
