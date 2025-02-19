package com.studit.backend.domain.todoList.service;

import com.studit.backend.domain.room.repository.StudyRoomRepository;
import com.studit.backend.domain.room.entity.StudyRoom;
import com.studit.backend.domain.todoList.dto.TodoCreateRequest;
import com.studit.backend.domain.todoList.dto.TodoListAllResponse;
import com.studit.backend.domain.todoList.dto.TodoResponse;
import com.studit.backend.domain.todoList.dto.TodoUpdateRequest;
import com.studit.backend.domain.todoList.entity.Enum.TodoEndType;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.todoList.entity.TodoLog;
import com.studit.backend.domain.todoList.repository.TodoLogRepository;
import com.studit.backend.domain.todoList.repository.TodoRepository;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudyRoomRepository studyRoomRepository;
    @Autowired
    private TodoLogRepository todoLogRepository;

    @Transactional
    public TodoListAllResponse findByUserIdAndStudyId(Long userId, Long studyId) {

       List<Todo> todos = todoRepository.findByUserIdAndStudyId(userId,studyId);
       TodoListAllResponse todoListAll = new TodoListAllResponse(todos);
        return todoListAll;
    }

    public TodoResponse createTodo(Long userid, TodoCreateRequest todoRequest) {
        User requestUser = userRepository.findById(userid).orElse(null);
        if (requestUser == null) {
            return null;
        }
        StudyRoom studyRoom = studyRoomRepository.findById(todoRequest.getStudyId()).orElse(null);

        if (studyRoom == null) {
            return null;
        }
        Todo newTodo= todoRepository.save(todoRequest.toEntity(requestUser,studyRoom));
       return new TodoResponse(newTodo);
    }

    public TodoResponse updateTodo(TodoUpdateRequest todoUpdateRequest) {
        Todo todo = todoRepository.findById(todoUpdateRequest.getTodoId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Todo가 존재하지 않습니다. ID: " + todoUpdateRequest.getTodoId()));
        todo.update(todoUpdateRequest.getTodoName());
       return new TodoResponse(todoRepository.save(todo));
    }

    public TodoResponse completeTodo(Long todoId, TodoEndType endYn) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Todo가 존재하지 않습니다. ID: " + todoId));

        todo.update(endYn);

        return new TodoResponse(todoRepository.save(todo));
    }


    public TodoResponse addTimeTodo(Long todoId, Long addTime) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Todo가 존재하지 않습니다. ID: " + todoId));

        todo.updateAddTime(addTime);
        Todo updateTodo = todoRepository.save(todo);
        addTodoLog(updateTodo,addTime);
        return new TodoResponse(updateTodo);
    }

    public void addTodoLog(Todo todo, Long addTime){
        TodoLog todoLog = new TodoLog(todo,addTime);
        todoLogRepository.save(todoLog);
    }
}
