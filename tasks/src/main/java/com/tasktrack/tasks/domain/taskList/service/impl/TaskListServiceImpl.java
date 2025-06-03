package com.tasktrack.tasks.domain.taskList.service.impl;

import com.tasktrack.tasks.domain.auth.dto.CustomOAuth2User;
import com.tasktrack.tasks.domain.auth.entity.UserEntity;
import com.tasktrack.tasks.domain.auth.repository.UserRepository;
import com.tasktrack.tasks.domain.taskList.dto.TaskListDTO;
import com.tasktrack.tasks.domain.taskList.entity.TaskList;
import com.tasktrack.tasks.domain.taskList.mapper.TaskListMapper;
import com.tasktrack.tasks.domain.taskList.repository.TaskListRepository;
import com.tasktrack.tasks.domain.taskList.service.TaskListService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final UserRepository userRepository;

    public TaskListServiceImpl(TaskListRepository taskListRepository, TaskListMapper taskListMapper,
                               UserRepository userRepository) {
        this.taskListRepository = taskListRepository;
        this.taskListMapper = taskListMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskListDTO> listTaskLists(Long userId) {
        return taskListRepository.findByUserId(userId).stream().map(taskListMapper::toDTO).toList();
    }

    @Override
    public TaskListDTO createTaskList(Long userId,
                                      TaskListDTO taskListDTO) {
        if (taskListDTO.id() != null) { // already created tasklist
            throw new IllegalArgumentException("Task list already has an ID");
        }
        // title required
        if (taskListDTO.title() == null || taskListDTO.title().isBlank()) {
            throw new IllegalArgumentException("Task list title must be present");
        }

        // find the currently logged in user
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalDateTime now = LocalDateTime.now();
        TaskList newTaskList = taskListRepository.save(
                new TaskList(
                        null,
                        taskListDTO.title(),
                        taskListDTO.description(),
                        null,
                        now,
                        now,
                        userEntity
                )
        );
        return taskListMapper.toDTO(newTaskList);
    }

    @Override
    public Optional<TaskListDTO> getTaskList(Long userId, UUID id) {
        return taskListRepository.findByUserIdAndId(userId, id).map(taskListMapper::toDTO);
    }

    @Transactional
    @Override
    public TaskListDTO updateTaskList(Long userId, UUID taskListId, TaskListDTO taskListDTO) {
        if (taskListDTO.id() == null) {
            throw new IllegalArgumentException("Task list must have an ID");
        }

        if (!taskListId.equals(taskListDTO.id())) {
            throw new IllegalArgumentException("Attempting to change task list ID, this is not permitted");
        }

        TaskList existingTaskList = taskListRepository.findByUserIdAndId(userId, taskListId).orElseThrow(() ->
                new IllegalArgumentException("task list not found"));

        existingTaskList.setTitle(taskListDTO.title());
        existingTaskList.setDescription(taskListDTO.description());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListMapper.toDTO(taskListRepository.save(existingTaskList));
    }

    @Override
    public void deleteTaskList(Long userId, UUID taskListId) {
        taskListRepository.deleteByUserIdAndId(userId, taskListId);
    }

}
