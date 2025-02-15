package com.studit.backend.domain.todoList.controller;

import com.studit.backend.domain.todoList.dto.TodoListAllResponse;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.todoList.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/{studyId}")
    public TodoListAllResponse getTodosByStudyId(@PathVariable Long studyId) {

        return null;// response;
    }


    @PostMapping("/new")
    public Todo createTodo(@RequestBody Todo todo) {
        return null;//todoService.createTodo(todo);
    }


    @PutMapping("/change")
    public Todo updateTodo(@RequestBody Todo todo) {
        return null;//todoService.updateTodo(todo);
    }


    @PatchMapping("/{todoId}/complete")
    public Todo completeTodo(@PathVariable Long todoId, @RequestBody Map<String, String> request) {
        String endYn = request.get("endYn");
        return null;//todoService.completeTodo(todoId, endYn);
    }
}


