package com.example.jee_project.note.controller.api;

import com.example.jee_project.note.dto.GetNotesResponse;
import com.example.jee_project.note.dto.PatchNoteRequest;
import com.example.jee_project.note.dto.PutNoteRequest;
import pl.edu.pg.eti.kask.rpg.character.dto.GetCharacterResponse;
import pl.edu.pg.eti.kask.rpg.character.dto.GetCharactersResponse;
import pl.edu.pg.eti.kask.rpg.character.dto.PatchCharacterRequest;
import pl.edu.pg.eti.kask.rpg.character.dto.PutCharacterRequest;

import java.io.InputStream;
import java.util.UUID;

/**
 * Controller for managing collections notes' representations.
 */
public interface NoteController {

    GetNotesResponse getNotes();
    GetNotesResponse getThreadsNotes(UUID id);
    GetNotesResponse getUserCharacters(UUID id);
    GetNotesResponse getCharacter(UUID uuid);
    void putCharacter(UUID id, PutNoteRequest request);
    void patchCharacter(UUID id, PatchNoteRequest request);
    void deleteCharacter(UUID id);
    byte[] getCharacterPortrait(UUID id);
    void putCharacterPortrait(UUID id, InputStream portrait);

}
