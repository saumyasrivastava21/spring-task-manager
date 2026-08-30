package com.example.spring_task_manager.dto;

import com.example.spring_task_manager.entity.AssignedUser;
import com.example.spring_task_manager.entity.Position;


public record UserDTO(String keycloakId, String email, Position position){

    public static UserDTO from(AssignedUser assignedUser) {
        return new UserDTO(assignedUser.getKeycloakId(), assignedUser.getEmail(), assignedUser.getPosition());
    }
};
