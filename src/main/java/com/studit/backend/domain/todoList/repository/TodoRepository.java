package com.studit.backend.domain.todoList.repository;

import com.studit.backend.domain.todoList.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
