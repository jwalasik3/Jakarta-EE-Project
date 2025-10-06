package com.example.jee_project.user.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class User implements Serializable {

    private UUID id;
    private String login;
    @ToString.Exclude
    private String password;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthday;
    private List<String> role;
}
