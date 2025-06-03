package com.tasktrack.tasks.controller;

import com.tasktrack.tasks.domain.auth.dto.CustomOAuth2User;
import com.tasktrack.tasks.domain.taskList.dto.TaskListDTO;
import com.tasktrack.tasks.domain.taskList.mapper.TaskListMapper;
import com.tasktrack.tasks.domain.taskList.service.TaskListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/task-lists")
public class TaskListController {

    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping
    public List<TaskListDTO> listTaskLists(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return taskListService.listTaskLists(customOAuth2User.getId());
    }

    @PostMapping
    public TaskListDTO createTaskList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                      @RequestBody TaskListDTO taskListDTO) {

        return taskListService.createTaskList(customOAuth2User.getId(), taskListDTO);
    }

    @GetMapping("/{task_list_id}")
    public Optional<TaskListDTO> getTaskList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                             @PathVariable(name = "task_list_id") UUID taskListId) {
        return taskListService.getTaskList(customOAuth2User.getId(), taskListId);
    }

    @PutMapping("/{task_list_id}")
    public TaskListDTO updateTaskList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                      @PathVariable(name = "task_list_id") UUID taskListId,
                                      @RequestBody TaskListDTO taskListDTO) {
        return taskListService.updateTaskList(customOAuth2User.getId(), taskListId, taskListDTO);
    }

    @DeleteMapping("/{task_list_id}")
    public void deleteTaskList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                               @PathVariable(name = "task_list_id") UUID taskListId) {
        taskListService.deleteTaskList(customOAuth2User.getId(), taskListId);
    }
}
