package com.studit.backend.domain.recruit.repository;

import com.studit.backend.domain.recruit.RecruitStatus;
import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.recruit.entity.StudyRecruit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRecruitRepository extends JpaRepository<StudyRecruit, Long> {
    Page<StudyRecruit> findByStatusOrderByRecruitStartAtDesc(RecruitStatus status, Pageable pageable);

    @Query("SELECT sr FROM StudyRecruit sr " +
            "WHERE sr.status = 'ACTIVE' " + // 모집 상태가 ACTIVE 인 것만 조회
            "AND (:title IS NULL OR sr.title LIKE %:title%) " +
            "AND (:category IS NULL OR sr.category = :category) " +
            "AND sr.deposit BETWEEN :minDeposit AND :maxDeposit " +
            "AND sr.goalTime BETWEEN :minGoalTime AND :maxGoalTime " +
            "AND (:tags IS NULL OR sr.tags LIKE %:tags%)")
    Page<StudyRecruit> findByFilters(
            @Param("title") String title,
            @Param("tags") String tags,
            @Param("category") StudyCategory category,
            @Param("minDeposit") int minDeposit,
            @Param("maxDeposit") int maxDeposit,
            @Param("minGoalTime") int minGoalTime,
            @Param("maxGoalTime") int maxGoalTime,
            Pageable pageable
    );
}
