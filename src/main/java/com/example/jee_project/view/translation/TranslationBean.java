package com.example.jee_project.view.translation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.Locale;
import java.util.ResourceBundle;

@Named("translation")
@ApplicationScoped
public class TranslationBean {

    public String get(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("translation.translation", locale);
        return bundle.getString(key);
    }
}