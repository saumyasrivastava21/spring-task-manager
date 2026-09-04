package com.example.spring_task_manager.controller;

import com.example.spring_task_manager.dto.RegisterRequest;
import com.example.spring_task_manager.dto.ResponsePage;
import com.example.spring_task_manager.dto.UserDTO;
import com.example.spring_task_manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping
    public ResponseEntity<String> createNewUser(@RequestBody RegisterRequest user) {
        var newUser = userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(String.format("User with email %s was created", user.email()));
    }
    @PostMapping("/all")
    public ResponseEntity<Void> createAllUsers(@RequestBody List<RegisterRequest> users) {
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
    @GetMapping("/paging")
    public ResponsePage<UserDTO> getUserPage(@RequestParam(name = "cursor", required = false) Long cursor,
                                             @RequestParam(name = "size") Long sizeOfPage) {

        return userService.getUserPage(cursor, sizeOfPage);
    }
}
