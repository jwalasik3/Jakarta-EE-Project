package com.example.jee_project.note.model;

import com.example.jee_project.note.entity.Importance;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ThreadModel {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class Note {
        private UUID id;
        private String title;
    }

    private UUID id;
    private String title;
    private Importance importance;
    private List<Note> notes;
}
