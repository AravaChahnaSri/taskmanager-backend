package com.chanu.taskmanager.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.chanu.taskmanager.entity.Task;
import com.chanu.taskmanager.entity.User;
import com.chanu.taskmanager.repository.TaskRepository;
import com.chanu.taskmanager.repository.UserRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // GET LOGGED-IN USER
    // =========================

    private User getLoggedInUser(Authentication authentication) {

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // =========================
    // CREATE TASK
    // =========================

    public Task createTask(Task task, Authentication authentication) {

        User user = getLoggedInUser(authentication);

        task.setUser(user);

        return taskRepository.save(task);
    }

    // =========================
    // GET ONLY USER'S TASKS
    // =========================

    public List<Task> getAllTasks(Authentication authentication) {

        User user = getLoggedInUser(authentication);

        return taskRepository.findByUserId(user.getId());
    }

    // =========================
    // GET SINGLE TASK
    // =========================

    public Task getTaskById(Long id,
                            Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return null;
        }

        // Security check
        if (!task.getUser().getId().equals(user.getId())) {
            return null;
        }

        return task;
    }

    // =========================
    // UPDATE TASK
    // =========================

    public Task updateTask(Long id,
                            Task task,
                            Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Task existingTask =
                taskRepository.findById(id).orElse(null);

        if (existingTask == null) {
            return null;
        }

        // Security check
        if (!existingTask.getUser().getId().equals(user.getId())) {
            return null;
        }

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setEstimatedTime(task.getEstimatedTime());

        return taskRepository.save(existingTask);
    }

    // =========================
    // DELETE TASK
    // =========================

    public boolean deleteTask(Long id,
                              Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Task task =
                taskRepository.findById(id).orElse(null);

        if (task == null) {
            return false;
        }

        // Security check
        if (!task.getUser().getId().equals(user.getId())) {
            return false;
        }

        taskRepository.delete(task);

        return true;
    }
}