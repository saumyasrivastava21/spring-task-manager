package com.example.spring_task_manager.controller;

import com.example.spring_task_manager.dto.TaskDTO;
import com.example.spring_task_manager.dto.UserDTO;
import com.example.spring_task_manager.entity.Status;
import com.example.spring_task_manager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok()
                .body(String.format("Task with id:%d was deleted", id));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }
    @PostMapping("/all")
    public ResponseEntity<Void> createAllTasks(@RequestBody List<TaskDTO> tasks) {
        taskService.createAllTasks(tasks);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long id, @RequestBody Status status) {
        taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(
                String.format("Status for task with id:%d was updated", id));
    }

    @PatchMapping("/{id}/description")
    public ResponseEntity<String> updateTaskDescription(@PathVariable Long id, @RequestBody String description) {
        taskService.updateTaskDescription(id, description);
        return ResponseEntity.ok(
                String.format("Description for task with id:%d was updated", id)
        );
    }

    @PatchMapping("/{id}/users")
    public ResponseEntity<String> assignUserToTask(@PathVariable Long id, @RequestParam Long userId) {
        taskService.assignUserToTheTask(id, userId);
        return ResponseEntity.ok(
                String.format("User with id:%d was assigned to task with id:%d", userId, id)
        );
    }
    @GetMapping("/paging")
    public List<TaskDTO> getUserPage(@RequestParam(name = "page") Long pageNumber,
                                     @RequestParam(name = "size") Long sizeOfPage) {

        return taskService.getTaskPage(pageNumber, sizeOfPage);
    }

}
