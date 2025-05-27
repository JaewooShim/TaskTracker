package com.tasktrack.tasks.controller;

import com.tasktrack.tasks.domain.task.dto.TaskDTO;
import com.tasktrack.tasks.domain.task.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-lists/{task_list_id}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getTasks(@PathVariable(name = "task_list_id") UUID taskListId) {
        return taskService.listTasks(taskListId);
    }

    @PostMapping
    public TaskDTO createTask(@PathVariable(name = "task_list_id") UUID taskListId, @RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskListId, taskDTO);
    }

    @GetMapping("/{task_id}")
    public Optional<TaskDTO> getTask(@PathVariable(name = "task_list_id") UUID taskListId,
                                     @PathVariable(name = "task_id") UUID taskId) {
        return taskService.getTask(taskListId, taskId);
    }

    @PutMapping("/{task_id}")
    public TaskDTO updateTask(@PathVariable(name = "task_list_id") UUID taskListId,
                              @PathVariable(name = "task_id") UUID taskId,
                              @RequestBody TaskDTO taskDTO) {
        return taskService.updateTask(taskListId, taskId, taskDTO);
    }

    @DeleteMapping("/{task_id}")
    public void deleteTask(@PathVariable(name = "task_list_id") UUID taskListId,
                           @PathVariable(name = "task_id") UUID taskId) {
        taskService.deleteTask(taskListId, taskId);
    }
}
