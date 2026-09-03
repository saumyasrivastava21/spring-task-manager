package com.example.spring_task_manager.controller;

import com.example.spring_task_manager.dto.ProjectDTO;
import com.example.spring_task_manager.dto.ProjectDTOCreateRequest;
import com.example.spring_task_manager.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects()
                .stream()
                .map(ProjectDTO::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createNewProject(@RequestBody ProjectDTOCreateRequest projectDTO) {
        return ResponseEntity.ok(projectService.createProject(projectDTO));
    }
    @PostMapping("/all")
    public ResponseEntity<Void> createAllProjects(@RequestBody List<ProjectDTOCreateRequest> projectDTOList) {
        projectService.createAllProjects(projectDTOList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping
    public ResponseEntity<String> deleteProject(@RequestParam("projectTitle") String projectTitle) {
        projectService.deleteProjectByName(projectTitle);
        return ResponseEntity.ok(String.format("Project with title:%s was deleted", projectTitle));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProjectDescription(@PathVariable Long id, @RequestBody String description) {
        return ResponseEntity.ok(projectService.changeDescription(id, description));
    }

    @PatchMapping("/{id}/users")
    public ResponseEntity<String> assignUserToTask(@PathVariable Long id, @RequestBody Long userId) {
        projectService.assignUserToProject(id, userId);
        return ResponseEntity.ok(String.format("User with id:%d was assigned to project succsessfully", userId));
    }

    @PatchMapping("/{id}/tasks")
    public ResponseEntity<String> addNewTask(@PathVariable Long id, @RequestParam Long taskId) {
        projectService.addNewTask(id, taskId);
        return ResponseEntity.ok(String.format("New task with id:%d to project was added", taskId));
    }
    @GetMapping("/paging")
    public List<ProjectDTO> getUserPage(@RequestParam(name = "page") Long pageNumber,
                                     @RequestParam(name = "size") Long sizeOfPage) {

        return projectService.getProjectPage(pageNumber, sizeOfPage);
    }
}
