package com.example.jee_project.note.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode

public class NoteEditModel {

    private String title;
    private String content;
    private Long version;
}
