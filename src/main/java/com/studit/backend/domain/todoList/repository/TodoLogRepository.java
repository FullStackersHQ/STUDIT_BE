package com.studit.backend.domain.todoList.repository;

import com.studit.backend.domain.todoList.entity.TodoLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoLogRepository extends JpaRepository<TodoLog, Long> {
}
