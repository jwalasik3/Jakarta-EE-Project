package com.example.jee_project.user.model.function;

import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.model.UsersModel;

import java.util.List;
import java.util.function.Function;

public class UsersToModelFunction implements Function<List<User>, UsersModel> {

    @Override
    public UsersModel apply(List<User> entity) {
        return UsersModel.builder()
                .users(entity.stream()
                        .map(character -> UsersModel.User.builder()
                                .id(character.getId())
                                .login(character.getLogin())
                                .build())
                        .toList())
                .build();
    }
}

