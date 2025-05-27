package com.tasktrack.tasks.domain.task.service;

import com.tasktrack.tasks.domain.task.dto.TaskDTO;
import com.tasktrack.tasks.domain.task.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<TaskDTO> listTasks(UUID taskListId);
    TaskDTO createTask(UUID taskListId, TaskDTO taskDTO);
    Optional<TaskDTO> getTask(UUID taskListId, UUID taskId);
    TaskDTO updateTask(UUID taskListId, UUID taskId, TaskDTO taskDTO);
    void deleteTask(UUID taskListId, UUID taskId);
}
