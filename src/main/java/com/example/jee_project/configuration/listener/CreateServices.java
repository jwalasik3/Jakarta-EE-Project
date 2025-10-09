package com.example.jee_project.configuration.listener;

import com.example.jee_project.crypto.component.Pbkdf2PasswordHash;
import com.example.jee_project.datastore.component.DataStore;
import com.example.jee_project.user.repository.api.UserRepository;
import com.example.jee_project.user.repository.memory.UserInMemoryRepository;
import com.example.jee_project.user.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class CreateServices implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        DataStore dataSource = (DataStore) event.getServletContext().getAttribute("datasource");

        UserRepository userRepository = new UserInMemoryRepository(dataSource);

        event.getServletContext().setAttribute("userService", new UserService(userRepository, new Pbkdf2PasswordHash()));
    }

}
