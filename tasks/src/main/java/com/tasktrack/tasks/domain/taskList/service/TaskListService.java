package com.tasktrack.tasks.domain.taskList.service;

import com.tasktrack.tasks.domain.taskList.dto.TaskListDTO;
import com.tasktrack.tasks.domain.taskList.entity.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListService {
    List<TaskListDTO> listTaskLists(Long userId);
    TaskListDTO createTaskList(Long userId, TaskListDTO taskListDTO);
    Optional<TaskListDTO> getTaskList(Long userId, UUID id);
    TaskListDTO updateTaskList(Long userId, UUID taskListId, TaskListDTO taskListDTO);
    void deleteTaskList(Long userId, UUID taskListId);
}
