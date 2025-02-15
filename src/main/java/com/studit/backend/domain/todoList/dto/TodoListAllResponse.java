package com.studit.backend.domain.todoList.dto;

import com.studit.backend.domain.todoList.entity.Todo;

import java.util.List;

import static com.studit.backend.domain.todoList.utils.TimeUtils.formatDuration;

public class TodoListAllResponse {

    String studyTotalTime;
    List<Todo> todoList;

    public TodoListAllResponse(List<Todo> todoList) {
        Long totalTime = 0L;
        for(Todo todo : todoList) {
          totalTime += todo.getTotalStudyTime();
        }
        studyTotalTime = formatDuration(totalTime);
        this.todoList = todoList;
    }
}
