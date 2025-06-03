package com.tasktrack.tasks.domain.taskList.repository;

import com.tasktrack.tasks.domain.taskList.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListRepository extends JpaRepository<TaskList, UUID> {
    List<TaskList> findByUserId(Long userId);

    Optional<TaskList> findByUserIdAndId(Long userId, UUID id);

    @Transactional
    void deleteByUserIdAndId(Long userId, UUID id);
}
