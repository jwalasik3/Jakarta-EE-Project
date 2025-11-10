package com.example.jee_project.note.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "note_threads")
public class NoteThread implements Serializable {

    @Id
    private UUID id;
    private String title;
    private Importance importance;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "noteThread", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Note> notes = new ArrayList<>();

    public void addNote(Note note) {
        if (notes == null) notes = new ArrayList<>();
        if (!notes.contains(note)) {
            notes.add(note);
            note.setNoteThread(this);
        }
    }

    public void removeNote(Note note) {
        if (notes != null && notes.remove(note)) {
            note.setNoteThread(null);
        }
    }
}
