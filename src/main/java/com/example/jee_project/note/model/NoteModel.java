package com.example.jee_project.note.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class NoteModel {

    private UUID id;
    private String title;
    private String content;
    private Long version;
    private LocalDateTime creationDateTime;
}
