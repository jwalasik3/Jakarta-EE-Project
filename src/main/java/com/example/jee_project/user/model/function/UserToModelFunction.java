package com.example.jee_project.user.model.function;

import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.model.UserModel;

import java.io.Serializable;
import java.util.function.Function;

public class UserToModelFunction implements Function<User, UserModel>, Serializable {

    @Override
    public UserModel apply(User entity) {
        return UserModel.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .build();
    }
}
