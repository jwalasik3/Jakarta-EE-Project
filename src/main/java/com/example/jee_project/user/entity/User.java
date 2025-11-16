package com.example.jee_project.user.entity;

import jakarta.persistence.*;
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
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    private UUID id;
    private String login;
    @ToString.Exclude
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private String name;
    private String surname;
    private LocalDate birthday;
    @CollectionTable(name = "users__roles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role;
}
