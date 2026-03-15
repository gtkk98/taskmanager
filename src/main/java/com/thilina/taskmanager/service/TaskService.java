package com.thilina.taskmanager.service;

import com.thilina.taskmanager.model.Task;
import com.thilina.taskmanager.model.Task.Status;
import com.thilina.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public Task createTask(Task task) {
        if (task.getAssignedTo() == null) {
            var user = userService.getUserById(task.getAssignedTo().getId());
            task.setAssignedTo(user);
        }
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public Page<Task> getAllTasks(Pageable  pageable) {
        return taskRepository.findAll(pageable);
    }

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAssigneeId(userId);
    }

    public Page<Task> getTaskByStatus(Status status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    public Task updateTask(Long id, Task updates) {
        Task existing =  getTaskById(id);
        existing.setTitle(updates.getTitle());
        existing.setDescription(updates.getDescription());
        existing.setStatus(updates.getStatus());
        existing.setDueDate(updates.getDueDate());
        if (updates.getAssignedTo() != null) {
            var user = userService.getUserById(updates.getAssignedTo().getId());
            existing.setAssignedTo(user);
        }
        return taskRepository.save(existing);
    }
}
