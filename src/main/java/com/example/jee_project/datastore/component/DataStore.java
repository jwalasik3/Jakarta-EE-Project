package com.example.jee_project.datastore.component;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.user.entity.User;
import com.example.jee_project.serialization.component.CloningUtility;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * For the sake of simplification instead of using real database this example is using a data source object which should
 * be put in servlet context in a single instance. In order to avoid {@link java.util.ConcurrentModificationException}
 * all methods are synchronized. Normally synchronization would be carried on by the database server. Caution, this is
 * very inefficient implementation but can be used to present other mechanisms without obscuration example with ORM
 * usage.
 */
@Log
public class DataStore {

    /**
     * Set of all available professions.
     */
    private final Set<NoteThread> noteThreads = new HashSet<>();

    /**
     * Set of all characters.
     */
    private final Set<Note> notes = new HashSet<>();

    /**
     * Set of all users.
     */
    private final Set<User> users = new HashSet<>();

    /**
     * Component used for creating deep copies.
     */
    private final CloningUtility cloningUtility;

    /**
     * @param cloningUtility component used for creating deep copies
     */
    public DataStore(CloningUtility cloningUtility) {
        this.cloningUtility = cloningUtility;
    }

    /**
     * Seeks for all professions.
     *
     * @return list (can be empty) of all professions
     */
    public synchronized List<NoteThread> findAllNoteThreads() {
        return noteThreads.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new profession.
     *
     * @param value new profession to be stored
     * @throws IllegalArgumentException if profession with provided id already exists
     */
    public synchronized void createNoteThread(NoteThread value) throws IllegalArgumentException {
        if (noteThreads.stream().anyMatch(profession -> profession.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The profession id \"%s\" is not unique".formatted(value.getId()));
        }
        noteThreads.add(cloningUtility.clone(value));
    }

    /**
     * Seeks for all characters.
     *
     * @return list (can be empty) of all characters
     */
    public synchronized List<Note> findAllNotes() {
        return notes.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new character.
     *
     * @param value new character to be stored
     * @throws IllegalArgumentException if character with provided id already exists or when {@link User} or
     *                                  {@link NoteThread} with provided uuid does not exist
     */
    public synchronized void createNote(Note value) throws IllegalArgumentException {
        if (notes.stream().anyMatch(note -> note.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The character id \"%s\" is not unique".formatted(value.getId()));
        }
        Note entity = cloneWithRelationships(value);
        notes.add(entity);
    }

    /**
     * Updates existing character.
     *
     * @param value character to be updated
     * @throws IllegalArgumentException if character with the same id does not exist or when {@link User} or
     *                                  {@link NoteThread} with provided uuid does not exist
     */
    public synchronized void updateNote(Note value) throws IllegalArgumentException {
        Note entity = cloneWithRelationships(value);
        if (notes.removeIf(character -> character.getId().equals(value.getId()))) {
            notes.add(entity);
        } else {
            throw new IllegalArgumentException("The character with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    /**
     * Deletes existing character.
     *
     * @param id id of character to be deleted
     * @throws IllegalArgumentException if character with provided id does not exist
     */
    public synchronized void deleteNote(UUID id) throws IllegalArgumentException {
        if (!notes.removeIf(character -> character.getId().equals(id))) {
            throw new IllegalArgumentException("The character with id \"%s\" does not exist".formatted(id));
        }
    }

    /**
     * Seeks for all users.
     *
     * @return list (can be empty) of all users
     */
    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores new user.
     *
     * @param value new user to be stored
     * @throws IllegalArgumentException if user with provided id already exists
     */
    public synchronized void createUser(User value) throws IllegalArgumentException {
        if (users.stream().anyMatch(character -> character.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The user id \"%s\" is not unique".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

    /**
     * Updates existing user.
     *
     * @param value user to be updated
     * @throws IllegalArgumentException if user with the same id does not exist
     */
    public synchronized void updateUser(User value) throws IllegalArgumentException {
        if (users.removeIf(user -> user.getId().equals(value.getId()))) {
            users.add(cloningUtility.clone(value));
        } else {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
    }

    /**
     * Clones existing character and updates relationships for values in storage
     *
     * @param value character
     * @return cloned value with updated relationships
     * @throws IllegalArgumentException when {@link User} or {@link NoteThread} with provided uuid does not exist
     */
    private Note cloneWithRelationships(Note value) {
        Note entity = cloningUtility.clone(value);

        if (entity.getUser() != null) {
            entity.setUser(users.stream()
                    .filter(user -> user.getId().equals(value.getUser().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No user with id \"%s\".".formatted(value.getUser().getId()))));
        }

        if (entity.getNoteThread() != null) {
            entity.setNoteThread(noteThreads.stream()
                    .filter(profession -> profession.getId().equals(value.getNoteThread().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No profession with id \"%s\".".formatted(value.getNoteThread().getId()))));
        }

        return entity;
    }

}
