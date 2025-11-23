package com.example.jee_project.chat.socket;


import com.example.jee_project.chat.entity.ChatMessage;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat/{username}")
public class ChatEndpoint {

    private static final Set<ChatEndpoint> endpoints = new CopyOnWriteArraySet<>();
    private static final Map<String, ChatEndpoint> users = new ConcurrentHashMap<>();
    private static final Jsonb JSONB = JsonbBuilder.create();
    private Session session;
    private String username;

    public static void broadcast(ChatMessage message) {
        String payload = JSONB.toJson(message);
        endpoints.forEach(endpoint -> {
            try {
                endpoint.session.getAsyncRemote().sendText(payload);
            } catch (Exception e) {
                removeEndpoint(endpoint);
            }
        });
    }

    public static void sendToUser(ChatMessage message, String toUser) {
        message.setFrom("[PRIVATE] " + message.getFrom());
        ChatEndpoint endpoint = users.get(toUser);
        if (endpoint != null && endpoint.session.isOpen()) {
            try {
                endpoint.session.getAsyncRemote().sendText(JSONB.toJson(message));
            } catch (Exception e) {
                removeEndpoint(endpoint);
            }
        }
    }

    private static void removeEndpoint(ChatEndpoint endpoint) {
        endpoints.remove(endpoint);
        if (endpoint.username != null) users.remove(endpoint.username);
        try {
            if (endpoint.session != null && endpoint.session.isOpen()) endpoint.session.close();
        } catch (IOException ignored) {
        }
    }

    private static void broadcastSystemMessage(String text) {
        ChatMessage msg = new ChatMessage();
        msg.setFrom("[SYSTEM]");
        msg.setContent(text);
        broadcast(msg);
    }

    public static void broadcastUserMessage(ChatMessage message) {
        message.setFrom("[PUBLIC] " + message.getFrom());
        broadcast(message);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        this.username = username;
        endpoints.add(this);
        users.put(username, this);

        broadcastSystemMessage(username + " connected!");
    }

    @OnClose
    public void onClose(Session session) {
        endpoints.remove(this);
        if (username != null) users.remove(username);
        broadcastSystemMessage(username + " disconnected!");
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        removeEndpoint(this);
    }
}
