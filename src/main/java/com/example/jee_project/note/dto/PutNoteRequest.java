package com.example.jee_project.note.dto;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.user.entity.User;

public class PutNoteRequest {

    private String title;
    private String content;
    private User user;
    private NoteThread noteThread;
}
