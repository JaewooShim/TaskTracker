package com.tasktrack.tasks.domain.taskList.service;

import com.tasktrack.tasks.domain.taskList.dto.TaskListDTO;
import com.tasktrack.tasks.domain.taskList.entity.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListService {
    List<TaskListDTO> listTaskLists();
    TaskListDTO createTaskList(TaskListDTO taskListDTO);
    Optional<TaskListDTO> getTaskList(UUID id);
    TaskListDTO updateTaskList(UUID taskListId, TaskListDTO taskListDTO);
    void deleteTaskList(UUID taskListId);
}
