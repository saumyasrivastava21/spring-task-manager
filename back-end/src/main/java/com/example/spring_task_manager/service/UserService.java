package com.example.spring_task_manager.service;

import com.example.spring_task_manager.dto.UserDTO;
import com.example.spring_task_manager.entity.AssignedUser;
import com.example.spring_task_manager.exceptions.UserAlreadyExistsInDataBase;
import com.example.spring_task_manager.exceptions.UserNotFoundException;
import com.example.spring_task_manager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::from)
                .toList();
    }
    public AssignedUser getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
    public AssignedUser getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(
                    "User not found"
            );
        }
        userRepository.deleteById(id);
    }
    public UserDTO createUser(UserDTO assignedUser) {
        if (userRepository.existsByEmail(assignedUser.email())) {
            throw new UserAlreadyExistsInDataBase(
                    String.format("User with this email {%s} already exists in database.",
                            assignedUser.email()));
        }
        var newAssignedUser =
                new AssignedUser(assignedUser.firstName(),
                        assignedUser.email(), assignedUser.position());

        return UserDTO.from(userRepository.save(newAssignedUser));
    }
    public void createAllUsers(List<UserDTO> users) {
        users.forEach(this::createUser);
    }
    public UserDTO updateUser(UserDTO assignedUser, Long id) {
        var entityFromDB = getUserById(id);

        entityFromDB.setEmail(assignedUser.email());
        entityFromDB.setFirstName(assignedUser.firstName());
        entityFromDB.setPosition(assignedUser.position());

        return UserDTO.from(userRepository.save(entityFromDB));
    }
}
