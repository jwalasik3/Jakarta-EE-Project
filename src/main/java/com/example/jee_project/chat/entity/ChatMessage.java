package com.example.jee_project.chat.entity;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatMessage implements Serializable {

    private String from;
    private String to;
    private String content;
}

