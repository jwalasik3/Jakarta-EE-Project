package com.example.jee_project.user.model.converter;
import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.model.UserModel;
import com.example.jee_project.user.service.UserService;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.util.Optional;

@FacesConverter(forClass = UserModel.class, managed = true)
public class UserModelConverter implements Converter<UserModel> {

    private UserService service;
    private final ModelFunctionFactory factory;

    @Inject
    public UserModelConverter(ModelFunctionFactory factory) {
        this.factory = factory;
    }

    @EJB
    public void setService(UserService service) {
        this.service = service;
    }

    @Override
    public UserModel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Optional<User> user = service.find(value);
        return user.map(factory.userToModel()).orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, UserModel value) {
        return value == null ? "" : value.getLogin();
    }

}
