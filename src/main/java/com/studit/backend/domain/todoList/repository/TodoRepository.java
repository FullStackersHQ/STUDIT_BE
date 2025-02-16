package com.studit.backend.domain.todoList.repository;

import com.studit.backend.domain.room.entity.StudyRoom;
import com.studit.backend.domain.todoList.entity.Todo;
import com.studit.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    //Studyroom과 연동시 쿼리 변경해야함
    @Query("SELECT t FROM TODOS t WHERE t.user.id = :userId AND t.study = :studyId")
    List<Todo> findByUserIdAndStudyId(@Param("userId") Long userId, @Param("studyId") Long studyId);
}