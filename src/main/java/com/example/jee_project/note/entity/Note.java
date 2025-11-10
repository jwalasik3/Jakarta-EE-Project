package com.example.jee_project.note.entity;

import com.example.jee_project.user.entity.User;
import jakarta.persistence.*;
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
@Entity
@Table(name = "notes")
public class Note implements Serializable {

    @Id
    private UUID id;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_login")
    private User user;
    @ManyToOne
    @JoinColumn(name = "noteThread")
    private NoteThread noteThread;
}
