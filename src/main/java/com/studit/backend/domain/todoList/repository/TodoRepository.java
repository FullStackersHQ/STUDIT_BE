package com.studit.backend.domain.todoList.repository;

import com.studit.backend.domain.study.entity.StudyRoom;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserIdAndStudyId(User userId, StudyRoom studyId);
}