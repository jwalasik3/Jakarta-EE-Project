package com.example.jee_project.servlet.filter;

import com.example.jee_project.servlet.holder.HttpServletResponseHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Instance of {@link HttpServletRequest} can be acquired with {@link jakarta.servlet.annotation.WebListener} but for
 * {@link HttpServletResponse} we need filter.
 */
@WebFilter(urlPatterns = "/*")
public class HttpServletResponseFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            HttpServletResponseHolder.INSTANCE.bind(response);
            chain.doFilter(request, response);
        } finally {
            HttpServletResponseHolder.INSTANCE.release();
        }
    }

}
