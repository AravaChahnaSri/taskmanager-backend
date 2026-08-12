package com.chanu.taskmanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanu.taskmanager.entity.Task;
import com.chanu.taskmanager.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody Task task,
            Authentication authentication) {

        return ResponseEntity.ok(
                taskService.createTask(task, authentication)
        );
    }

    // =========================
    // GET ALL USER TASKS
    // =========================

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            Authentication authentication) {

        return ResponseEntity.ok(
                taskService.getAllTasks(authentication)
        );
    }

    // =========================
    // GET SINGLE TASK
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Long id,
            Authentication authentication) {

        Task task =
                taskService.getTaskById(id, authentication);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    // =========================
    // UPDATE
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody Task task,
            Authentication authentication) {

        Task updatedTask =
                taskService.updateTask(
                        id,
                        task,
                        authentication
                );

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    // =========================
    // DELETE
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {

        boolean deleted =
                taskService.deleteTask(
                        id,
                        authentication
                );

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}