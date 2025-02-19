package com.studit.backend.domain.todoList.controller;

import com.studit.backend.domain.todoList.dto.TodoCreateRequest;
import com.studit.backend.domain.todoList.dto.TodoListAllResponse;
import com.studit.backend.domain.todoList.dto.TodoResponse;
import com.studit.backend.domain.todoList.dto.TodoUpdateRequest;
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

    /**
     * @ getTodosByStudyIdTest test를위해서 만든 api
     * @return :
     */
    @GetMapping(value = "/test")
    public ResponseEntity<TodoListAllResponse> getTodosByStudyIdTest() {

        TodoListAllResponse todoListAllResponse = todoService.findByUserIdAndStudyId(1L,1L);
        return ResponseEntity.ok().body(todoListAllResponse);// response;
    }

    @PostMapping("/new")
    public ResponseEntity<TodoResponse> createTodo(@RequestHeader("Authorization") String token, @RequestBody TodoCreateRequest todoRequest) {
        Long userid = jwtTokenProvider.getUserIdFromToken(token);

        TodoResponse todoResponse = todoService.createTodo(userid, todoRequest);
        if(todoResponse == null){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(todoResponse);
    }


    @PutMapping("/change")
    public ResponseEntity<TodoResponse> updateTodo(@RequestHeader("Authorization") String token, @RequestBody TodoUpdateRequest todo) {
        TodoResponse todoResponse = todoService.updateTodo(todo);
         return ResponseEntity.ok().body(todoResponse);
    }


    @PatchMapping("/{todoId}/complete")
    public ResponseEntity<TodoResponse> completeTodo(@PathVariable Long todoId, @RequestBody boolean endYn) {
        TodoResponse todoResponse = todoService.completeTodo(todoId, endYn? TodoEndType.Y: TodoEndType.N);

        return ResponseEntity.ok().body(todoResponse);
    }


    @PatchMapping("/{todoId}/addTimeTest")
    public ResponseEntity<TodoResponse> addTimeTodo(@PathVariable Long todoId, @RequestBody Long addTime) {
        TodoResponse todoResponse = todoService.addTimeTodo(todoId, addTime);

        return ResponseEntity.ok().body(todoResponse);
    }
}


