package com.studit.backend.domain.todoList.entity;

import com.studit.backend.domain.study.entity.StudyRoom;
import com.studit.backend.domain.todoList.entity.Enum.TodoEndType;
import com.studit.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "TODOS")
@Getter
@Builder
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @Column(name = "todo_name", nullable = false)
    private String todoName;

    // 총 사용시간 컬럼 (초 단위로 저장)
    @Column(name = "total_study_time", nullable = false)
    @ColumnDefault("0")
    private Long totalStudyTime = 0L; // 기본값 0초로 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

  //  @ManyToOne(fetch = FetchType.LAZY)
  //  @JoinColumn(name = "study_Id", nullable = false)
  //  private StudyRoom study;

     @Column(name = "study_Id", nullable = false)
      private Long study;


    // 생성일시 컬럼
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "end_yn", nullable = false, length = 1)
    @ColumnDefault("'N'")
    private TodoEndType endYN = TodoEndType.N;



    @PrePersist
    public void setDefaultValues() {
        if (endYN == null) {
            endYN = TodoEndType.N; // 기본값 N로 설정
        }

        if (totalStudyTime == null) {
            totalStudyTime = 0L; // 기본값 0초로 설정
        }
    }

    public Todo(){}

    public Todo(StudyRoom study, User user,String todoName){
       // this.study = study;
        this.user = user;
        this.todoName = todoName;
    }
    public Todo(Long study, User user,String todoName){
        this.study = study;
        this.user = user;
        this.todoName = todoName;
    }


    // 수정 메서드 추가
    public void update(String todoName) {
        this.todoName = todoName;
    }

    public void update(TodoEndType endYn) {
        this.endYN = endYn;
    }
}
