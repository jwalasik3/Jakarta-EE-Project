package com.example.jee_project.note.entity;

import com.example.jee_project.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Note implements Serializable {

    private UUID id;
    private String title;
    private String content;
    private User user;
    private NoteThread noteThread;
}
