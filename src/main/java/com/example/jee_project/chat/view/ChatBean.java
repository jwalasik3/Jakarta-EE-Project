package com.example.jee_project.chat.view;

import com.example.jee_project.chat.entity.ChatEvent;
import com.example.jee_project.chat.entity.ChatMessage;
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
    private Event<ChatEvent> event;

    private String message;
    private String to;

    public void send() {
        String user = getUser();
        if (user == null || message == null || message.isBlank()) return;

        ChatMessage msg = ChatMessage.builder()
                .from(user)
                .content(message)
                .to((to == null || to.isBlank()) ? null : to.trim())
                .build();

        event.fire(new ChatEvent(msg));

        message = "";
    }

    private String getUser() {
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
}
