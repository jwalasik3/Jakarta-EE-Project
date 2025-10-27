package com.example.jee_project.note.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class NoteCreateModel {

    private UUID id;
    private String title;
    private String content;
    private ThreadModel thread;
}
