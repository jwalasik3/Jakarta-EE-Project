package com.example.jee_project.chat.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatEvent {

    private final ChatMessage message;
}

