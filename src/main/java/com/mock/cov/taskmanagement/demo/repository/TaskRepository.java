package com.mock.cov.taskmanagement.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mock.cov.taskmanagement.demo.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Spring Data JPA provides: findAll, findById, save, delete, etc.

}
