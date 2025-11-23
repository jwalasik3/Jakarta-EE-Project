package com.example.jee_project.chat.listener;

import com.example.jee_project.chat.entity.ChatEvent;
import com.example.jee_project.chat.entity.ChatMessage;
import com.example.jee_project.chat.socket.ChatEndpoint;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class ChatEventListener {

    public void onChatMessage(@Observes ChatEvent event) {
        ChatMessage msg = event.getMessage();
        if (msg.getTo() == null || msg.getTo().isBlank()) {
            ChatEndpoint.broadcastUserMessage(msg);
        } else {
            ChatEndpoint.sendToUser(msg, msg.getTo());
        }
    }
}
