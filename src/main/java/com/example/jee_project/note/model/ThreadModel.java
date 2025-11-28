package com.example.jee_project.note.model;

import com.example.jee_project.note.entity.Importance;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ThreadModel {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Note {
        @EqualsAndHashCode.Include
        private UUID id;
        private String title;
        private Long version;
        private LocalDateTime modificationDateTime;
        private LocalDateTime creationDateTime;
    }

    @EqualsAndHashCode.Include
    private UUID id;
    private String title;
    private Importance importance;
    private List<Note> notes;
}
