package com.example.spring_task_manager.service;

import com.example.spring_task_manager.dto.ProjectDTO;
import com.example.spring_task_manager.dto.ProjectDTOCreateRequest;
import com.example.spring_task_manager.dto.ResponsePage;
import com.example.spring_task_manager.dto.TaskDTO;
import com.example.spring_task_manager.entity.Project;
import com.example.spring_task_manager.exceptions.EmptyFetchedResults;
import com.example.spring_task_manager.exceptions.ProjectAlreadyExists;
import com.example.spring_task_manager.exceptions.ProjectDoNotExist;
import com.example.spring_task_manager.repository.ProjectRepository;
import com.example.spring_task_manager.repository.TaskRepository;
import com.example.spring_task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public ProjectDTO createProject(ProjectDTOCreateRequest project) {
        if (projectRepository.existsByName(project.name())) {
            throw new ProjectAlreadyExists(
                    String.format("Project with that title \"%s\" already exists", project.name()));
        }
        var newProject = new Project(project.name(), project.description());
        return ProjectDTO.from(projectRepository.save(newProject));
    }

    public void createAllProjects(List<ProjectDTOCreateRequest> projects) {
        projects.forEach(this::createProject);
    }

    public List<Project> getAllProjects() {
        var projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new EmptyFetchedResults("No projects present");
        }
        return projectRepository.findAll();
    }

    public Project getProjectByName(String projectName) {
        return projectRepository.findByName(projectName).orElseThrow();
    }

    public void deleteProjectByName(String projectName) {
        if (!projectRepository.existsByName(projectName)) {
            throw new ProjectDoNotExist(
                    String.format("Project with that name \"%s\" does not exist", projectName));
        }
        var project = projectRepository.findByName(projectName).orElseThrow();
        projectRepository.delete(project);
    }

    public void addNewTask(Long id, Long taskId) {
        var project = projectRepository.findById(id).orElseThrow();
        var task = taskRepository.findById(taskId).orElseThrow();
        task.setProject(project);

        taskRepository.save(task);
    }
    public void assignUserToProject(Long projectId, Long userId) {
        var project = projectRepository.findById(projectId).orElseThrow();
        var user = userRepository.findById(userId).orElseThrow();

        user.setProject(project);
        userRepository.save(user);
    }

    public ProjectDTO changeDescription(Long projectId, String description) {
        var project = projectRepository.findById(projectId).orElseThrow();
        project.setDescription(description);

        return ProjectDTO.from(projectRepository.save(project));
    }

    public ResponsePage<ProjectDTO> getProjectPage(Long cursor, Long sizeOfPage) {
        var data = projectRepository.fetchData(cursor, sizeOfPage);
        boolean hasNext = data.size() == sizeOfPage;
        Long nextCursor = hasNext ? data.get(data.size() - 1).getId() : null;

        var dataDTO = data.stream()
                .map(ProjectDTO::from)
                .toList();
        return new ResponsePage<>(dataDTO, nextCursor, hasNext, sizeOfPage);
    }
}
