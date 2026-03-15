package com.thilina.taskmanager.repository;

import com.thilina.taskmanager.model.Task;
import com.thilina.taskmanager.model.Task.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssigneeId(Long userId);
    Page<Task> findByStatus(Status status, Pageable pageable);
}
