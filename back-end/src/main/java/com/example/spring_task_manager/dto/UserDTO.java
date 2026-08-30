package com.example.spring_task_manager.dto;

import com.example.spring_task_manager.entity.AssignedUser;
import com.example.spring_task_manager.entity.Position;

import java.util.List;

public record UserDTO(String firstName, String email, Position position){

    public static UserDTO from(AssignedUser assignedUser) {
        return new UserDTO(assignedUser.getFirstName(), assignedUser.getEmail(), assignedUser.getPosition());
    }
};
