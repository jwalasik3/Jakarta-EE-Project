package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.PatchNoteThreadRequest;
import com.example.jee_project.note.entity.NoteThread;

import java.util.function.BiFunction;

public class UpdateNoteThreadWithRequestFunction implements BiFunction<NoteThread, PatchNoteThreadRequest, NoteThread> {

    @Override
    public NoteThread apply(NoteThread entity, PatchNoteThreadRequest request) {

        return NoteThread.builder()
                .id(entity.getId())
                .title(request.getTitle() != null ? request.getTitle() : entity.getTitle())
                .importance(request.getImportance() != null ? request.getImportance() : entity.getImportance())
                .notes(request.getNotes() != null ? request.getNotes() : entity.getNotes())
                .build();
    }
}
