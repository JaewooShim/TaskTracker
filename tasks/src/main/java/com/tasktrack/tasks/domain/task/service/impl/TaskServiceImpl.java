package com.tasktrack.tasks.domain.task.service.impl;

import com.tasktrack.tasks.domain.task.dto.TaskDTO;
import com.tasktrack.tasks.domain.task.entity.Task;
import com.tasktrack.tasks.domain.task.entity.TaskPriority;
import com.tasktrack.tasks.domain.task.entity.TaskStatus;
import com.tasktrack.tasks.domain.task.mapper.TaskMapper;
import com.tasktrack.tasks.domain.task.repository.TaskRepository;
import com.tasktrack.tasks.domain.task.service.TaskService;
import com.tasktrack.tasks.domain.taskList.entity.TaskList;
import com.tasktrack.tasks.domain.taskList.repository.TaskListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<TaskDTO> listTasks(UUID taskListId) {
        return taskRepository.findByTaskListId(taskListId).stream().map(taskMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public TaskDTO createTask(UUID taskListId, TaskDTO taskDTO) {
        if (taskDTO.id() != null) {
            throw new IllegalArgumentException("Task already has an Id");
        }

        if (taskDTO.title() == null || taskDTO.title().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        // default priority = medium
        TaskPriority taskPriority = Optional.ofNullable(taskDTO.priority()).orElse(TaskPriority.MEDIUM);
        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task list ID"));
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task(
                null,
                taskDTO.title(),
                taskDTO.description(),
                taskDTO.dueDate(),
                taskStatus,
                taskPriority,
                taskList,
                now,
                now
        );
        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public Optional<TaskDTO> getTask(UUID taskListId, UUID taskId) {
        return taskRepository.findByTaskListIdAndId(taskListId, taskId).map(taskMapper::toDTO);
    }

    @Transactional
    @Override
    public TaskDTO updateTask(UUID taskListId, UUID taskId, TaskDTO taskDTO) {
        if (taskDTO.id() == null || !taskId.equals(taskDTO.id())) {
            throw new IllegalArgumentException("Invalid Task to update");
        }
        if (taskDTO.title() == null || taskDTO.title().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (taskDTO.priority() == null) {
            throw new IllegalArgumentException("Task must have a priority");
        }
        if (taskDTO.status() == null) {
            throw new IllegalArgumentException("Task must have a status");
        }
        Task task = taskRepository.findByTaskListIdAndId(taskListId, taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setDueDate(taskDTO.dueDate());
        task.setPriority(taskDTO.priority());
        task.setStatus(taskDTO.status());
        task.setUpdated(LocalDateTime.now());

        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Transactional
    @Override
    public void deleteTask(UUID taskListId, UUID taskId) {
        taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
    }
}
