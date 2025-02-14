package com.studit.backend.domain.study.repository;

import com.studit.backend.domain.recruit.RegisterStatus;
import com.studit.backend.domain.study.StudyRoomStatus;
import com.studit.backend.domain.study.entity.StudyRoom;
import com.studit.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    Page<StudyRoom> findAllByLeader(User leader, Pageable pageable);

    // 사용자가 참여한 모든 스터디 조회
    @Query("SELECT sr FROM StudyRoom sr JOIN StudyPartcpntMgnt spm ON sr = spm.room WHERE spm.user = :user")
    Page<StudyRoom> findAllByParticipant(@Param("user") User user, Pageable pageable);


    Page<StudyRoom> findByStatusAndStudyStartAtAfter(
            StudyRoomStatus status, LocalDateTime startAt, Pageable pageable);


    Page<StudyRoom> findByStatusAndStudyEndAtBefore(
            StudyRoomStatus status, LocalDateTime endAt, Pageable pageable);
}
