package com.example.jee_project.note.dto;


import com.example.jee_project.note.entity.Importance;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PutNoteThreadRequest {

    private String title;
    private Importance importance;
    private List<UUID> notes;
}
