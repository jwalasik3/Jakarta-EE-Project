package com.example.jee_project.component;

import com.example.jee_project.user.dto.function.UserToResponseFunction;
import com.example.jee_project.user.dto.function.UsersToResponseFunction;

public class DtoFunctionFactory {

    public UserToResponseFunction userToResponseFunction() {

        return new UserToResponseFunction();
    }

    public UsersToResponseFunction usersToResponseFunction() {

        return new UsersToResponseFunction();
    }
}
