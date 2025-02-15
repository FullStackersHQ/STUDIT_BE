package com.studit.backend.domain.todoList.service;

import com.studit.backend.domain.todoList.dto.TodoListAllResponse;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.todoList.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;


    public TodoListAllResponse findByUserIdAndStudyId(Long userId, Long studyId) {

       List<Todo> todos = todoRepository.findByUserIdAndStudyId(userId,studyId);
       TodoListAllResponse todoListAllResponse = new TodoListAllResponse(todos);



        return todoListAllResponse;
    }

}
