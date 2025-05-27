package com.tasktrack.tasks.domain.taskList.mapper;

import com.tasktrack.tasks.domain.taskList.dto.TaskListDTO;
import com.tasktrack.tasks.domain.taskList.entity.TaskList;

public interface TaskListMapper {
    TaskList fromDTO(TaskListDTO taskListDTO);
    TaskListDTO toDTO(TaskList taskList);
}
