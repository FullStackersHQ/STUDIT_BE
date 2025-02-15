package com.studit.backend.domain.todoList.controller;

import com.studit.backend.domain.todoList.dto.TodoListAllResponse;
import com.studit.backend.domain.todoList.entity.Enum.TodoEndType;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.todoList.service.TodoService;
import com.studit.backend.global.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{studyId}")
    public ResponseEntity<TodoListAllResponse> getTodosByStudyId(@RequestHeader("Authorization") String token, @PathVariable Long studyId) {
        Long userid = jwtTokenProvider.getUserIdFromToken(token);
        TodoListAllResponse todoListAllResponse = todoService.findByUserIdAndStudyId(userid,studyId);
        return ResponseEntity.ok().body(todoListAllResponse);// response;
    }

    @GetMapping(value = "/test",produces = "application/json" )
    public ResponseEntity<TodoListAllResponse> getTodosByStudyId1() {

        TodoListAllResponse todoListAllResponse = todoService.findByUserIdAndStudyId(1L,1L);
        return ResponseEntity.ok().body(todoListAllResponse);// response;
    }

    @PostMapping("/new")
    public Todo createTodo(@RequestHeader("Authorization") String token,@RequestBody Todo todo) {
        Long userid = jwtTokenProvider.getUserIdFromToken(token);

        return null;//todoService.createTodo(todo);
    }


    @PutMapping("/change")
    public Todo updateTodo(@RequestHeader("Authorization") String token, @RequestBody Todo todo) {
        return null;//todoService.updateTodo(todo);
    }


    @PatchMapping("/{todoId}/complete")
    public Todo completeTodo(@PathVariable Long todoId, @RequestBody boolean request) {
        TodoEndType endYn = request ? TodoEndType.Y : TodoEndType.N;
        return null;//todoService.completeTodo(todoId, endYn);
    }
}


