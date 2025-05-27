package com.tasktrack.tasks.controller;

import com.tasktrack.tasks.domain.taskList.dto.TaskListDTO;
import com.tasktrack.tasks.domain.taskList.mapper.TaskListMapper;
import com.tasktrack.tasks.domain.taskList.service.TaskListService;
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
    public List<TaskListDTO> listTaskLists() {
        return taskListService.listTaskLists();
    }

    @PostMapping
    public TaskListDTO createTaskList(@RequestBody TaskListDTO taskListDTO) {
        return taskListService.createTaskList(taskListDTO);
    }

    @GetMapping("/{task_list_id}")
    public Optional<TaskListDTO> getTaskList(@PathVariable(name = "task_list_id") UUID taskListId) {
        return taskListService.getTaskList(taskListId);
    }

    @PutMapping("/{task_list_id}")
    public TaskListDTO updateTaskList(@PathVariable(name = "task_list_id") UUID taskListId,
                                      @RequestBody TaskListDTO taskListDTO) {
        return taskListService.updateTaskList(taskListId, taskListDTO);
    }

    @DeleteMapping("/{task_list_id}")
    public void deleteTaskList(@PathVariable(name = "task_list_id") UUID taskListId) {
        taskListService.deleteTaskList(taskListId);
    }
}
