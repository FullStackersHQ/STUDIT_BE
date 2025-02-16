package com.studit.backend.domain.todoList.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.studit.backend.domain.todoList.entity.Todo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.studit.backend.domain.todoList.utils.TimeUtils.formatDuration;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
public class TodoListAllResponse {

    String studyTotalTime;
    List<Todo> todoList;

    public TodoListAllResponse(List<Todo> todoList) {

        if(todoList.isEmpty()) {
            System.out.println(formatDuration(0L));
            this.studyTotalTime = formatDuration(0L);
            this.todoList = new ArrayList<Todo>();
            System.out.println(this.studyTotalTime);
        }else {
            Long totalTime = 0L;
            for (Todo todo : todoList) {
                totalTime += todo.getTotalStudyTime();
            }

            studyTotalTime = formatDuration(totalTime);
            this.todoList = todoList;
        }
    }

}
