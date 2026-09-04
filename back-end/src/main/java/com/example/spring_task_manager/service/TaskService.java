package com.example.spring_task_manager.service;

import com.example.spring_task_manager.dto.ResponsePage;
import com.example.spring_task_manager.dto.TaskDTO;
import com.example.spring_task_manager.entity.Status;
import com.example.spring_task_manager.entity.Task;
import com.example.spring_task_manager.exceptions.TaskAlreadyExists;
import com.example.spring_task_manager.exceptions.TaskDoNotExist;
import com.example.spring_task_manager.repository.TaskRepository;
import com.example.spring_task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final UserRepository userRepository;
    private TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDTO createTask(TaskDTO task) {
        if(taskRepository.existsByTitle(task.title())) {
           throw
                   new TaskAlreadyExists(
                           String.format("Task with that title \"%s\" already exists", task.title()));
        }
        var newTask = new Task(task.title(), task.description(), task.status(), task.deadLine(), task.priority());
        return TaskDTO.from(taskRepository.save(newTask));
    }

    public void createAllTasks(List<TaskDTO> tasks) {
        tasks.forEach(this::createTask);
    }
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskDTO::from)
                .toList();
    }
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }
    public void updateTaskStatus(Long id, Status status) {
        var existedTask = getTaskById(id);
        existedTask.setStatus(status);

        taskRepository.save(existedTask);
    }
    public void updateTaskDescription(Long id, String description) {
        var existedTask = getTaskById(id);
        existedTask.setDescription(description);

        taskRepository.save(existedTask);
    }
    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskDoNotExist(
                    String.format("Task with id \"%d\" does not exist", id)
            );
        }
        taskRepository.deleteById(id);
    }
    public void assignUserToTheTask(Long taskId, Long userId) {
        var task = taskRepository.findById(taskId).orElseThrow();
        var user = userRepository.findById(userId).orElseThrow();

        task.setAssignedUser(user);
        taskRepository.save(task);
    }

    public ResponsePage<TaskDTO> getTaskPage(Long cursor, Long sizeOfPage) {
        var data = taskRepository.fetchPage(cursor, sizeOfPage);
        boolean hasNext = data.size() == sizeOfPage;
        Long nextCursor = hasNext ? data.get(data.size() - 1).getId() : null;

        var dataDTO = data.stream()
                .map(TaskDTO::from)
                .toList();
        return new ResponsePage<>(dataDTO, nextCursor, hasNext, sizeOfPage);
    }
}
