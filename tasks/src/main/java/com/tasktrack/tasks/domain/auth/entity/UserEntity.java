package com.tasktrack.tasks.domain.auth.entity;

import com.tasktrack.tasks.domain.taskList.entity.TaskList;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<TaskList> taskLists;

    @Builder
    public UserEntity(String username, String name, String email, UserRole userRole) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }
}
