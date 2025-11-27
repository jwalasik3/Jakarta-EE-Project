package com.example.jee_project.note.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetNoteResponse {

    private UUID id;
    private String title;
    private String content;
    private Long version;
}
