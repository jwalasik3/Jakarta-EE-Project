package com.example.jee_project.note.dto;

import com.example.jee_project.note.entity.Importance;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetNoteThreadResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Note {

        private UUID id;
        private String title;
    }

    private UUID id;
    private String title;
    private Importance importance;
    private List<Note> notes;
}
