package com.example.jee_project.note.dto;


import com.example.jee_project.note.entity.Importance;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PutNoteThreadRequest {

    private String title;
    private Importance importance;
}
