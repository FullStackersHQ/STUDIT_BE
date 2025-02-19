package com.studit.backend.domain.todoList.dto;

import com.studit.backend.domain.room.entity.StudyRoom;
import com.studit.backend.domain.room.repository.StudyRoomRepository;
import com.studit.backend.domain.todoList.entity.Enum.TodoEndType;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.user.entity.User;
import lombok.Getter;

@Getter
public class TodoCreateRequest {

    private Long studyId;
    private String todoName;

    public Todo toEntity( User user,StudyRoom studyRoom){

        return Todo.builder()
                .study(studyRoom)
                .user(user)
                .todoName(this.todoName)
                .build();
    }

}
