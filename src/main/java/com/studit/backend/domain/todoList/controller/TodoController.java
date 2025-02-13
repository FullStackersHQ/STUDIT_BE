package com.studit.backend.domain.todoList.controller;

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
    public Map<String, Object> getTodosByStudyId(@PathVariable Long studyId) {
       // List<Todo> todos = todoService.getTodosByStudyId(studyId);

        // 총 공부 시간 계산 (임시 값)
        String totalStudyTime = "27:51:12";

        Map<String, Object> response = new HashMap<>();
        response.put("studyTotalTime", totalStudyTime);
       // response.put("todos", todos);
        return null;// response;
    }

    // ✅ TODO 리스트 생성
    @PostMapping("/new")
    public Todo createTodo(@RequestBody Todo todo) {
        return null;//todoService.createTodo(todo);
    }

    // ✅ TODO 리스트 수정
    @PutMapping("/change")
    public Todo updateTodo(@RequestBody Todo todo) {
        return null;//todoService.updateTodo(todo);
    }

    // ✅ TODO 리스트 완료 처리
    @PatchMapping("/{todoId}/complete")
    public Todo completeTodo(@PathVariable Long todoId, @RequestBody Map<String, String> request) {
        String endYn = request.get("endYn");
        return null;//todoService.completeTodo(todoId, endYn);
    }
}


