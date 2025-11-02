package com.example.jee_project.note.dto;


import com.example.jee_project.note.entity.Importance;
import com.example.jee_project.note.entity.Note;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PatchNoteThreadRequest {

    private String title;
    private String content;
    private Importance importance;
    private List<Note> notes;
}
