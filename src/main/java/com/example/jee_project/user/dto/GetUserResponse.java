package com.example.jee_project.user.dto;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;

import java.util.List;

public class GetUserResponse {

    private String login;
    private List<Note> notes;
}
