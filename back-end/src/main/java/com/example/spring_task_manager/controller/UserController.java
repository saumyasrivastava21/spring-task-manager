package com.example.spring_task_manager.controller;

import com.example.spring_task_manager.dto.RegisterRequest;
import com.example.spring_task_manager.dto.UserDTO;
import com.example.spring_task_manager.entity.Position;
import com.example.spring_task_manager.service.KeycloakService;
import com.example.spring_task_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KeycloakService keycloakService;
    public UserController(UserService userService, KeycloakService keycloakService) {
        this.userService = userService;
        this.keycloakService = keycloakService;
    }
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping
    public ResponseEntity<String> createNewUser(@RequestBody RegisterRequest user) {
        String keycloakUserId = keycloakService.createUser(user);
        System.out.println("KeyCloakUserId: " + keycloakUserId);
        var newUser = userService.createUser(new UserDTO(keycloakUserId, user.email(), Position.valueOf(user.position())));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(String.format("User with email %s was created", user.email()));
    }
    @PostMapping("/all")
    public ResponseEntity<Void> createAllUsers(@RequestBody List<UserDTO> users) {
        userService.createAllUsers(users);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok()
                .body(String.format("User with id:%d was deleted", id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO user) {
        return ResponseEntity.ok().body(userService.updateUser(user, id));
    }
}
