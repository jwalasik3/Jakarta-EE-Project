package com.example.jee_project.note.entity;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class NoteThread implements Serializable {

    private UUID id;
    private String title;
    private List<Note> notes;
}
