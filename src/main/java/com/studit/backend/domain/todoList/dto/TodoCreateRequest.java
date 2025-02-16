package com.studit.backend.domain.todoList.dto;

import com.studit.backend.domain.study.entity.StudyRoom;
import com.studit.backend.domain.todoList.entity.Enum.TodoEndType;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.user.entity.User;
import lombok.Getter;

@Getter
public class TodoCreateRequest {

    private Long studyId;
    private String todoName;

    public Todo toEntity(StudyRoom studyRoom, User user){

        return Todo.builder()
                //.study(studyRoom)
                .user(user)
                .todoName(this.todoName)
                .build();
    }
    public Todo toEntity(User user){

        return Todo.builder()
                //   .study(studyRoom)
                .study(this.studyId)
                .user(user)
                .todoName(this.todoName)
                .build();
    }

}
