package com.example.jee_project.configuration.listener;

import com.example.jee_project.datastore.component.DataStore;
import com.example.jee_project.serialization.component.CloningUtility;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener started automatically on servlet context initialized. Creates an instance of datasource and puts it in the
 * application (servlet) context.
 */
@WebListener//using annotation does not allow configuring order
public class CreateDataSource implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("datasource", new DataStore(new CloningUtility()));
    }

}

