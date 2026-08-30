package com.example.spring_task_manager.dto;

public record RegisterRequest(
        String email,
        String password,
        String position
) {
}
