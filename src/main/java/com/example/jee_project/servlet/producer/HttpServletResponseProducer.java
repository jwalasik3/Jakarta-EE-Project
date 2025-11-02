package com.example.jee_project.servlet.producer;

import com.example.jee_project.servlet.holder.HttpServletResponseHolder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Creates {@link HttpServletResponse} using global {@link HttpServletResponseHolder} with {@link ThreadLocal}
 * functionality. Response object should be set in servlet filter.
 */
public class HttpServletResponseProducer {

    /**
     *
     * @return managed HTTP response
     */
    @Produces
    @RequestScoped
    HttpServletResponse create() {

        return HttpServletResponseHolder.INSTANCE.get();
    }

}
