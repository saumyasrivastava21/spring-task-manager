package com.example.spring_task_manager.service;

import com.example.spring_task_manager.dto.RegisterRequest;
import com.example.spring_task_manager.dto.UserDTO;
import com.example.spring_task_manager.entity.AssignedUser;
import com.example.spring_task_manager.entity.Position;
import com.example.spring_task_manager.exceptions.UserAlreadyExistsInDataBase;
import com.example.spring_task_manager.exceptions.UserNotFoundException;
import com.example.spring_task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private KeycloakService keycloakService;
    public UserService(UserRepository userRepository, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
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
    @Transactional
    public UserDTO createUser(RegisterRequest assignedUser) {

        String keycloakUserId = keycloakService.createUser(assignedUser);
        AssignedUser newUser = null;
        try {
            if (userRepository.existsByEmail(assignedUser.email())) {
                throw new UserAlreadyExistsInDataBase(
                        String.format("User with this email {%s} already exists in database.",
                                assignedUser.email()));
            }
            var newAssignedUser =
                    new AssignedUser(keycloakUserId, assignedUser.email(), Position.valueOf(assignedUser.position()));
            newUser = userRepository.save(newAssignedUser);
        } catch (Exception e) {
            keycloakService.deleteUser(keycloakUserId);
            System.out.println(e.getStackTrace());
        }
        return UserDTO.from(newUser);
    }
    public void createAllUsers(List<RegisterRequest> users) {
        users.forEach(this::createUser);
    }
    public UserDTO updateUser(UserDTO assignedUser, Long id) {
        var entityFromDB = getUserById(id);

        entityFromDB.setEmail(assignedUser.email());
        entityFromDB.setPosition(assignedUser.position());

        return UserDTO.from(userRepository.save(entityFromDB));
    }
}
