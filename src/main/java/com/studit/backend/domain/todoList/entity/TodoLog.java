package com.studit.backend.domain.todoList.entity;

import com.studit.backend.domain.todoList.entity.Enum.TodoEndType;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "TODO_LOG")
public class TodoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todoId;

    @JoinColumn(name = "add_time", nullable = false)
    @ColumnDefault("0")
    private Long addTime;

    @JoinColumn(name = "total_study_time", nullable = false)
    @ColumnDefault("0")
    private Long totalStudyTime;
    public TodoLog(){}
    public TodoLog(Todo todo, Long addTime){
        this.todoId = todo;
        this.addTime = addTime;
        this.totalStudyTime = todo.getTotalStudyTime();
    }

    @PrePersist
    public void setDefaultValues() {
        if (addTime == null) {
            addTime = 0L;
        }

        if (totalStudyTime == null) {
            totalStudyTime = 0L; // 기본값 0초로 설정
        }
    }
}
