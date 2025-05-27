package com.tasktrack.tasks.domain.task.mapper;

import com.tasktrack.tasks.domain.task.dto.TaskDTO;
import com.tasktrack.tasks.domain.task.entity.Task;

public interface TaskMapper {
    Task fromDTO(TaskDTO taskDTO);

    TaskDTO toDTO(Task task);
}
