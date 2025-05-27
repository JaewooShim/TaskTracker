package com.tasktrack.tasks.domain.taskList.dto;

import com.tasktrack.tasks.domain.task.dto.TaskDTO;

import java.util.List;
import java.util.UUID;

public record TaskListDTO(
        UUID id,
        String title,
        String description,
        Integer count,
        Double progress,
        List<TaskDTO> tasks
) {
}
