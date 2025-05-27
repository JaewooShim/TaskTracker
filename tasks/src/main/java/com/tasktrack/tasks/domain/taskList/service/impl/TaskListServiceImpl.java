package com.tasktrack.tasks.domain.taskList.service.impl;

import com.tasktrack.tasks.domain.taskList.dto.TaskListDTO;
import com.tasktrack.tasks.domain.taskList.entity.TaskList;
import com.tasktrack.tasks.domain.taskList.mapper.TaskListMapper;
import com.tasktrack.tasks.domain.taskList.repository.TaskListRepository;
import com.tasktrack.tasks.domain.taskList.service.TaskListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    private final TaskListMapper taskListMapper;

    public TaskListServiceImpl(TaskListRepository taskListRepository, TaskListMapper taskListMapper) {
        this.taskListRepository = taskListRepository;
        this.taskListMapper = taskListMapper;
    }

    @Override
    public List<TaskListDTO> listTaskLists() {
        return taskListRepository.findAll().stream().map(taskListMapper::toDTO).toList();
    }

    @Override
    public TaskListDTO createTaskList(TaskListDTO taskListDTO) {
        if (taskListDTO.id() != null) { // already created tasklist
            throw new IllegalArgumentException("Task list already has an ID");
        }
        // title required
        if (taskListDTO.title() == null || taskListDTO.title().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present");
        }

        LocalDateTime now = LocalDateTime.now();
        TaskList newTaskList = taskListRepository.save(
                new TaskList(
                        null,
                        taskListDTO.title(),
                        taskListDTO.description(),
                        null,
                        now,
                        now
                )
        );
        return taskListMapper.toDTO(newTaskList);
    }

    @Override
    public Optional<TaskListDTO> getTaskList(UUID id) {
        return taskListRepository.findById(id).map(taskListMapper::toDTO);
    }

    @Transactional
    @Override
    public TaskListDTO updateTaskList(UUID taskListId, TaskListDTO taskListDTO) {
        if (taskListDTO.id() == null) {
            throw new IllegalArgumentException("Task list must have an ID");
        }

        if (!taskListId.equals(taskListDTO.id())) {
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted");
        }

        TaskList existingTaskList = taskListRepository.findById(taskListId).orElseThrow(() ->
                new IllegalArgumentException("task list not found"));

        existingTaskList.setTitle(taskListDTO.title());
        existingTaskList.setDescription(taskListDTO.description());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListMapper.toDTO(taskListRepository.save(existingTaskList));
    }

    @Override
    public void deleteTaskList(UUID taskListId) {
        taskListRepository.deleteById(taskListId);
    }

}
