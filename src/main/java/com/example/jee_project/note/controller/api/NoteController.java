package com.example.jee_project.note.controller.api;

import com.example.jee_project.note.dto.GetNoteResponse;
import com.example.jee_project.note.dto.PatchNoteRequest;
import com.example.jee_project.note.dto.PutNoteRequest;

import java.io.InputStream;
import java.util.UUID;

/**
 * Controller for managing collections notes' representations.
 */
public interface NoteController {

    GetNoteResponse getNotes();
    GetNoteResponse getThreadsNotes(UUID id);
    GetNoteResponse getUserCharacters(UUID id);
    GetNoteResponse getCharacter(UUID uuid);
    void putCharacter(UUID id, PutNoteRequest request);
    void patchCharacter(UUID id, PatchNoteRequest request);
    void deleteCharacter(UUID id);
    byte[] getCharacterPortrait(UUID id);
    void putCharacterPortrait(UUID id, InputStream portrait);

}
