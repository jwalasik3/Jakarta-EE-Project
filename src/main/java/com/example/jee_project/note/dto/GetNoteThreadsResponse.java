package com.example.jee_project.note.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetNoteThreadsResponse {

    List<String> noteThreads;
}
