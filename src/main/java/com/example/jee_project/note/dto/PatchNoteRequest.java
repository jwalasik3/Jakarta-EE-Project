package com.example.jee_project.note.dto;

import com.example.jee_project.note.entity.NoteThread;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PatchNoteRequest {

    private String title;
    private String content;
    private NoteThread noteThread;
}
