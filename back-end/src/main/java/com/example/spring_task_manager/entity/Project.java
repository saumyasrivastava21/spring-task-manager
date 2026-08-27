package com.example.spring_task_manager.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String description;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    @OneToMany(mappedBy = "project", fetch=FetchType.LAZY)
    private List<AssignedUser> assignedUsers = new ArrayList<>();

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<AssignedUser> getAssignedUsers() {
        return List.copyOf(assignedUsers);
    }

    public void assignUser(AssignedUser user) {
        assignedUsers.add(user);
    }
}
