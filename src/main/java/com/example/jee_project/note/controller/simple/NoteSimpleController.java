//package com.example.jee_project.Note.controller.simple;
//
//import com.example.jee_project.Note.controller.api.NoteController;
//import com.example.jee_project.Note.dto.GetNotesResponse;
//import com.example.jee_project.Note.dto.PatchNoteRequest;
//import com.example.jee_project.Note.dto.PutNoteRequest;
//
//import java.io.InputStream;
//import java.util.UUID;
//
//public class NoteSimpleController implements NoteController {
//
//    private final NoteService service;
//
//    public NoteSimpleController(NoteService service) {
//        this.service = service;
//    }
//
//    @Override
//    public GetNotesResponse getNotes() {
//        return null;
//    }
//
//    @Override
//    public GetNotesResponse getThreadsNotes(UUID id) {
//        return null;
//    }
//
//    @Override
//    public GetNotesResponse getUserCharacters(UUID id) {
//        return null;
//    }
//
//    @Override
//    public GetNotesResponse getCharacter(UUID uuid) {
//        return null;
//    }
//
//    @Override
//    public void putCharacter(UUID id, PutNoteRequest request) {
//
//    }
//
//    @Override
//    public void patchCharacter(UUID id, PatchNoteRequest request) {
//
//    }
//
//    @Override
//    public void deleteCharacter(UUID id) {
//
//    }
//
//    @Override
//    public byte[] getCharacterPortrait(UUID id) {
//        return new byte[0];
//    }
//
//    @Override
//    public void putCharacterPortrait(UUID id, InputStream portrait) {
//
//    }
//}
