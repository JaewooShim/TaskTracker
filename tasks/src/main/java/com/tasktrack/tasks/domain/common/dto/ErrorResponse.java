package com.tasktrack.tasks.domain.common.dto;

public record ErrorResponse(
        int status,
        String message,
        String details
) {
}
