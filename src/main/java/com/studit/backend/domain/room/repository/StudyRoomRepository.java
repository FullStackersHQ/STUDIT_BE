package com.studit.backend.domain.room.repository;

import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.room.RoomStatus;
import com.studit.backend.domain.room.entity.StudyRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    Page<StudyRoom> findByStatusOrderByStudyStartAtDesc(RoomStatus status, Pageable pageable);

    @Query("SELECT sr FROM StudyRoom sr " +
            "WHERE sr.status = 'ACTIVE' " + // 스터디룸 상태가 ACTIVE 인 것만 조회
            "AND (:title IS NULL OR sr.title LIKE %:title% OR sr.tags LIKE %:title%) " +  // tags 포함 검색
            "AND (:category IS NULL OR sr.category = :category) " +
            "AND sr.deposit BETWEEN :minDeposit AND :maxDeposit " +
            "AND sr.goalTime BETWEEN :minGoalTime AND :maxGoalTime")
    Page<StudyRoom> findByFilters(
            @Param("title") String title,
            @Param("category") StudyCategory category,
            @Param("minDeposit") int minDeposit,
            @Param("maxDeposit") int maxDeposit,
            @Param("minGoalTime") int minGoalTime,
            @Param("maxGoalTime") int maxGoalTime,
            Pageable pageable
    );
}
