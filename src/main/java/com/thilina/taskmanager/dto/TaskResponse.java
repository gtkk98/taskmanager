package com.thilina.taskmanager.dto;

import com.thilina.taskmanager.model.Task;
import com.thilina.taskmanager.model.Task.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Status status,
        LocalDate dueDate,
        String assignedToUsername,
        LocalDateTime createdAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null,
                task.getCreatedAt()
        );
    }
}
