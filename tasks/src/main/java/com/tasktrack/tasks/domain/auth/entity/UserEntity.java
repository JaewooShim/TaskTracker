package com.tasktrack.tasks.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Builder
    public UserEntity(String username, String name, String email, UserRole userRole) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }
}
