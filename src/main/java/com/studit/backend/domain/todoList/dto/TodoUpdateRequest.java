package com.studit.backend.domain.todoList.dto;

import com.studit.backend.domain.room.entity.StudyRoom;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.user.entity.User;
import lombok.Getter;

@Getter
public class TodoUpdateRequest {
    private Long studyId;
    private Long todoId;
    private String todoName;
}
