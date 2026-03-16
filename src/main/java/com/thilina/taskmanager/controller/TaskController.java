package com.thilina.taskmanager.controller;

import com.thilina.taskmanager.dto.TaskRequest;
import com.thilina.taskmanager.dto.TaskResponse;
import com.thilina.taskmanager.model.Task;
import com.thilina.taskmanager.model.Task.Status;
import com.thilina.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest req) {
        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .status(req.status())
                .dueDate(req.dueDate())
                .assignedTo(req.assignedToId() != null
                        ? Task.builder().id(req.assignedToId()).build().getAssignedTo()
                        : null)
                .build();

        if (req.assignedToId() != null) {
            task.setAssignedTo(new com.thilina.taskmanager.model.User());
            task.getAssignedTo().setId(req.assignedToId());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TaskResponse.from(taskService.createTask(task)));
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable).map(TaskResponse::from));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                taskService.getTasksByUser(userId).stream()
                        .map(TaskResponse::from)
                        .toList()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TaskResponse>> getByStatus(
            @PathVariable Status status, Pageable pageable) {
        return ResponseEntity.ok(
                taskService.getTaskByStatus(status, pageable).map(TaskResponse::from)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest req) {
        Task updates = Task.builder()
                .title(req.title())
                .description(req.description())
                .status(req.status())
                .dueDate(req.dueDate())
                .build();
        if (req.assignedToId() != null) {
            updates.setAssignedTo(new com.thilina.taskmanager.model.User());
            updates.getAssignedTo().setId(req.assignedToId());
        }
        return ResponseEntity.ok(TaskResponse.from(taskService.updateTask(id, updates)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
