package com.studit.backend.domain.todoList.dto;

import com.studit.backend.domain.todoList.entity.Enum.TodoEndType;
import com.studit.backend.domain.todoList.entity.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.studit.backend.domain.todoList.utils.TimeUtils.formatDuration;

@Getter
public class TodoResponse {
    Long todoId;
    String todoName;

    LocalDateTime createdAt;
    TodoEndType endType;
    String totalStudyTime;

    public TodoResponse(Todo todo){
        this.todoId = todo.getTodoId();
        this.endType =todo.getEndYN();
        this.todoName = todo.getTodoName();
        this.createdAt = todo.getCreatedAt();
        this.totalStudyTime = formatDuration(todo.getTotalStudyTime());
    }
}
