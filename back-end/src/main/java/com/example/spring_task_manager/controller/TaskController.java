package com.example.spring_task_manager.controller;

import com.example.spring_task_manager.dto.ResponsePage;
import com.example.spring_task_manager.dto.TaskDTO;
import com.example.spring_task_manager.dto.UserDTO;
import com.example.spring_task_manager.entity.Priority;
import com.example.spring_task_manager.entity.Status;
import com.example.spring_task_manager.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks(@RequestParam(name = "status", required = false) Status status,
                                     @RequestParam(name = "beforeDate", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     LocalDateTime before,
                                     @RequestParam(name = "left", required = false) Integer daysLeftUntilDeadline,
                                     @RequestParam(name = "inactiveStatus", required = false) Status inactive,
                                     @RequestParam(name = "userId", required = false) Long userId,
                                     @RequestParam(name = "priority", required = false) Priority priority,
                                     @RequestParam(name = "after", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                     LocalDateTime after) {
        if (status == null && before == null && inactive == null) {
            return taskService.getTasksNearTheirDeadline(daysLeftUntilDeadline);
        } else if (status == null && inactive == null && after == null) {
            return taskService.getTasksBeforeDate(before);
        } else if (inactive != null) {
            return taskService.getTasksThatNotInactive(inactive);
        } else if (userId != null && before == null) {
            return taskService.getTasksByUserIdAndStatus(userId, status);
        } else if (priority != null) {
            return taskService.getTasksByPriorityAndStatus(status, priority);
        } else if (before != null && after != null && userId != null) {
            return taskService.getTasksCompletedByUserBetweenDates(userId, after, before);
        }
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
    @GetMapping("/")
    public ResponsePage<TaskDTO> getUserPage(@RequestParam(name = "cursor", required = false) Long cursor,
                                    @RequestParam(name = "size") Long sizeOfPage) {

        return taskService.getTaskPage(cursor, sizeOfPage);
    }

}
