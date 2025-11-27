package com.example.jee_project.note.dto;

import com.example.jee_project.user.entity.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PutNoteRequest {

    private String title;
    private String content;
    private User user;
    private Long version;
}
