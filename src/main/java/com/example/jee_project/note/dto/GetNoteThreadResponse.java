package com.example.jee_project.note.dto;

import com.example.jee_project.note.entity.Importance;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetNoteThreadResponse {

    private UUID id;
    private String title;
    private Importance importance;
}
