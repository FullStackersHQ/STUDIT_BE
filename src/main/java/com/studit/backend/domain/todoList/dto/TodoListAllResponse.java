package com.studit.backend.domain.todoList.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.studit.backend.domain.todoList.entity.Todo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.studit.backend.domain.todoList.utils.TimeUtils.formatDuration;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
public class TodoListAllResponse {

    String studyTotalTime;

    List<TodoResponse> todoList;

    public TodoListAllResponse(List<Todo> todoList) {

        if(todoList.isEmpty()) {
            System.out.println(formatDuration(0L));
            this.studyTotalTime = formatDuration(0L);
            this.todoList =  Collections.emptyList();
            System.out.println(this.studyTotalTime);
        }else {

            Long totalTime = todoList.stream()
                    .mapToLong(Todo::getTotalStudyTime)
                    .sum();

            studyTotalTime = formatDuration(totalTime);

            this.todoList = todoList.stream()
                    .map(TodoResponse::new)
                    .toList(); // Java 16 이상에서 사용 가능
        }
    }

}
