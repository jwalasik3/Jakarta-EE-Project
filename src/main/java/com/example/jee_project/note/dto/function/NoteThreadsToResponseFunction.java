package com.example.jee_project.note.dto.function;

import com.example.jee_project.note.dto.GetNoteThreadResponse;
import com.example.jee_project.note.dto.GetNoteThreadsResponse;
import com.example.jee_project.note.entity.NoteThread;

import java.util.List;
import java.util.function.Function;

public class NoteThreadsToResponseFunction implements Function<List<NoteThread>, GetNoteThreadsResponse> {

    @Override
    public GetNoteThreadsResponse apply(List<NoteThread> noteThreads) {

        return GetNoteThreadsResponse.builder()
                .noteThreads(noteThreads.stream().map(NoteThread::getTitle).toList())
                .build();
    }
}
