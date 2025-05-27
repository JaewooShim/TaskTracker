package com.tasktrack.tasks.domain.taskList.repository;

import com.tasktrack.tasks.domain.taskList.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

}
