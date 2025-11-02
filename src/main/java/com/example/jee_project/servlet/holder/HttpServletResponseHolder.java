package com.example.jee_project.servlet.holder;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Holds instance of {@link HttpServletResponse} for particular {@link Thread} using {@link ThreadLocal}.
 */
public class HttpServletResponseHolder {

    /**
     * Stores instance bound to specific thread.
     */
    private final ThreadLocal<HttpServletResponse> value = new ThreadLocal<>();

    /**
     * Public global instance.
     */
    public final static HttpServletResponseHolder INSTANCE = new HttpServletResponseHolder();

    private HttpServletResponseHolder() {

    }

    /**
     * Bounds response to current thread.
     *
     * @param response new response to be bound to current thread
     */
    public void bind(HttpServletResponse response) {

        if (value.get() != null) {
            throw new IllegalStateException("Instance already bound to this thread.");
        }
        value.set(response);
    }

    /**
     * Releases response instance from current thread.
     */
    public void release() {

        value.remove();
    }

    /**
     * @return response instance bound to this thread
     */
    public HttpServletResponse get() {

        if (value.get() == null) {
            throw new IllegalStateException("No active HTTP request.");
        }
        return value.get();
    }

}
