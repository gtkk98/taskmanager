package com.thilina.taskmanager.dto;

import com.thilina.taskmanager.model.User.Role;

public record UserRequest(
        String username,
        String email,
        Role role
) {}
