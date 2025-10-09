package com.example.jee_project.user.dto.function;

import com.example.jee_project.user.dto.GetUserResponse;
import com.example.jee_project.user.entity.User;

import java.util.function.Function;

public class UserToResponseFunction implements Function<User, GetUserResponse> {

    @Override
    public GetUserResponse apply(User user) {

        return GetUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .birthday(user.getBirthday())
                .surname(user.getSurname())
                .email(user.getEmail())
                .build();
    }

}
