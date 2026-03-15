package com.thilina.taskmanager.repository;

import com.thilina.taskmanager.model.Task.Status;
import com.thilina.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssigneeId(Long userId);
    Page<Task> findByStatus(Status status, Pageable pageable);

}
