package com.example.jee_project.chat.socket;

import com.example.jee_project.chat.entity.ChatMessage;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat/{username}")
public class ChatEndpoint {

    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    // map username -> endpoint (easier to send direct messages)
    private static final Map<String, ChatEndpoint> users = new ConcurrentHashMap<>();
    private static final Jsonb JSONB = JsonbBuilder.create();

    private Session session;
    private String username;

    private static void removeEndpoint(ChatEndpoint endpoint) {
        try {
            chatEndpoints.remove(endpoint);
            if (endpoint.username != null) {
                users.remove(endpoint.username);
            }
            try {
                if (endpoint.session != null && endpoint.session.isOpen()) endpoint.session.close();
            } catch (IOException ignore) {
            }
        } catch (Exception ignore) {
        }
    }

    private static void broadcast(ChatMessage message) {
        String payload = JSONB.toJson(message);
        chatEndpoints.forEach(endpoint -> {
            try {
                endpoint.session.getAsyncRemote().sendText(payload, result -> {
                    if (!result.isOK()) {
                        System.err.println("Failed to send to " + endpoint.username + ": " + result.getException());
                        removeEndpoint(endpoint);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                removeEndpoint(endpoint);
            }
        });
    }

    private static void sendToUserDirect(ChatMessage message, String toUsername) {
        ChatEndpoint endpoint = users.get(toUsername);
        if (endpoint != null && endpoint.session != null && endpoint.session.isOpen()) {
            try {
                endpoint.session.getAsyncRemote().sendText(JSONB.toJson(message), result -> {
                    if (!result.isOK()) {
                        System.err.println("Failed direct send to " + toUsername + ": " + result.getException());
                        removeEndpoint(endpoint);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                removeEndpoint(endpoint);
            }
        }
    }

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("username") String username) {

        this.session = session;
        this.username = username;
        chatEndpoints.add(this);
        users.put(username, this);

        ChatMessage message = new ChatMessage();
        message.setFrom(username);
        message.setContent("Connected!");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(String messagePayload, Session session) {
        try {
            ChatMessage incoming = JSONB.fromJson(messagePayload, ChatMessage.class);
            // If incoming.to is present -> private, otherwise broadcast
            if (incoming.getTo() == null || incoming.getTo().isBlank()) {
                // public
                incoming.setFrom(this.username);
                broadcast(incoming);
            } else {
                incoming.setFrom(this.username);
                sendToUserDirect(incoming, incoming.getTo());
            }
        } catch (Exception e) {
            // fallback: treat raw payload as public text
            ChatMessage fallback = new ChatMessage();
            fallback.setFrom(this.username);
            fallback.setContent(messagePayload);
            broadcast(fallback);
        }
    }

    @OnClose
    public void onClose(Session session) {
        removeEndpoint(this);
        if (this.username != null) {
            ChatMessage message = new ChatMessage();
            message.setFrom(this.username);
            message.setContent("Disconnected!");
            broadcast(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        System.err.println("ChatEndpoint error for user=" + this.username + ": " + thr);
        removeEndpoint(this);
    }
}