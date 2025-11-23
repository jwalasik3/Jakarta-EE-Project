package com.example.jee_project.chat.view;

import com.example.jee_project.chat.entity.ChatEvent;
import com.example.jee_project.chat.entity.ChatMessage;
import com.example.jee_project.chat.socket.ChatEndpoint;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
@Getter
@Setter
public class ChatBean {

    @Inject
    Event<ChatEvent> event;

    private String message;
    private String to;

    public void send() {
        ChatMessage msg = new ChatMessage();
        msg.setFrom(getUser());
        msg.setTo((to == null || to.isBlank()) ? null : to.trim());
        msg.setContent(message);

        ChatEndpoint.broadcastToUser(msg);
    }



    private String getUser() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
}
