package com.tasktrack.tasks.domain.task.dto;

import com.tasktrack.tasks.domain.task.entity.TaskPriority;
import com.tasktrack.tasks.domain.task.entity.TaskStatus;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status
) {
}
