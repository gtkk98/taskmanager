package com.thilina.taskmanager.dto;

import com.thilina.taskmanager.model.Task.Status;

import java.time.LocalDate;

public record TaskRequest(
        String title,
        String description,
        Status status,
        LocalDate dueDate,
        Long assignedToId
) {}
