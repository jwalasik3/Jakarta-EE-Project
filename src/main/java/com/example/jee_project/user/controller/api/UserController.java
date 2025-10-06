package com.example.jee_project.user.controller.api;

import com.example.jee_project.user.dto.GetUserResponse;
import com.example.jee_project.user.dto.GetUsersResponse;

import java.util.UUID;

public interface UserController {

    GetUsersResponse getUsers();
    GetUserResponse getUser(UUID id);
    byte[] getUserAvatar(UUID id);
}
