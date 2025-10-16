package com.example.jee_project.controller.servlet;

import com.example.jee_project.user.controller.api.UserController;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Central API servlet for fetching all request from the client and preparing responses. Servlet API does not allow
 * named path parameters so wildcard is used.
 */
@WebServlet(urlPatterns = {
        ApiServlet.Paths.API + "/*"
})
@MultipartConfig(maxFileSize = 200 * 1024)
public class ApiServlet extends HttpServlet {

    private UserController userController;

    public static final class Paths {

        public static final String API = "/api";
    }

    /**
     * Patterns used for checking servlet path.
     */
    public static final class Patterns {

        /**
         * UUID
         */
        private static final Pattern UUID = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

        /**
         * All notes.
         */
        public static final Pattern NOTES = Pattern.compile("/notes/?");

        /**
         * Single note.
         */
        public static final Pattern NOTE = Pattern.compile("/notes/(%s)".formatted(UUID.pattern()));

        public static final Pattern USERS = Pattern.compile("/users");

        public static final Pattern USER = Pattern.compile("/users/(%s)".formatted(UUID.pattern()));

        /**
         * All notes of single user.
         */
        public static final Pattern USER_NOTES = Pattern.compile("/users/(%s)/notes/?".formatted(UUID.pattern()));

        /**
         * Single user's portrait.
         */
        public static final Pattern USER_AVATAR = Pattern.compile("/users/(%s)/avatar".formatted(UUID.pattern()));

        /**
         * All note threads.
         */
        public static final Pattern THREADS = Pattern.compile("/threads/?");

        /**
         * All notes of single thread.
         */
        public static final Pattern THREAD_NOTES = Pattern.compile("/threads/(%s)/notes/?".formatted(UUID.pattern()));


    }

    /**
     * JSON-B mapping object. According to open liberty documentation creating this is expensive. The JSON-B is only one
     * of many solutions. JSON strings can be built by hand {@link StringBuilder} or with JSON-P API. Both JSON-B and
     * JSON-P are part of Jakarta EE whereas JSON-B is newer standard.
     */
    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    public ApiServlet(UserController userController) {

        this.userController = userController;
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getMethod().equals("PATCH")) {
//            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            if (path.matches(Patterns.USERS.pattern())) {
                response.setContentType("application/json");
                response.getWriter().write(jsonb.toJson(userController.getUsers()));
                return;
            } else if (path.matches(Patterns.USER.pattern())) {
                response.setContentType("application/json");
                UUID uuid = extractUuid(Patterns.USER, path);
                response.getWriter().write(jsonb.toJson(userController.getUser(uuid)));
                return;
            } else if (path.matches(Patterns.USER_AVATAR.pattern())) {
                response.setContentType("image/png");//could be dynamic but atm we support only one format
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                byte[] portrait = userController.getUserAvatar(uuid);
                response.setContentLength(portrait.length);
                response.getOutputStream().write(portrait);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            /*if (path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                userController.createUser(uuid, request.getPart("avatar").getInputStream());
                response.addHeader("Location", createUrl(request, Paths.API, "notes", uuid.toString()));
                return;
            }
            else*/
            if (path.matches(Patterns.USER_AVATAR.pattern())) {
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                userController.putUserAvatar(uuid, request.getPart("avatar").getInputStream());
                response.addHeader("Location", createUrl(request, Paths.API, "notes", uuid.toString()));
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = parseRequestPath(request);
        String servletPath = request.getServletPath();
        if (Paths.API.equals(servletPath)) {
            /*if (path.matches(Patterns.USER.pattern())) {
                UUID uuid = extractUuid(Patterns.USER, path);
                userController.deleteUser(uuid);
                return;
            }
            else */
            if (path.matches(Patterns.USER_AVATAR.pattern())) {
                UUID uuid = extractUuid(Patterns.USER_AVATAR, path);
                userController.deleteAvatar(uuid);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private static UUID extractUuid(Pattern pattern, String path) {

        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return UUID.fromString(matcher.group(1));
        }
        throw new IllegalArgumentException("No UUID in path.");
    }

    /**
     * Gets path info from the request and returns it. No null is returned, instead empty string is used.
     *
     * @param request original servlet request
     * @return path info (not null)
     */
    private String parseRequestPath(HttpServletRequest request) {

        String path = request.getPathInfo();
        path = path != null ? path : "";
        return path;
    }

    public static String createUrl(HttpServletRequest request, String... paths) {

        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath());
        for (String path : paths) {
            builder.append("/")
                    .append(path, path.startsWith("/") ? 1 : 0, path.endsWith("/") ? path.length() - 1 : path.length());
        }
        return builder.toString();
    }

}
