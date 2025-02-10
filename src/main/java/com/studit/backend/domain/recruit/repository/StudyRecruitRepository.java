package com.studit.backend.recruit.repository;

import com.studit.backend.recruit.RecruitStatus;
import com.studit.backend.recruit.entity.StudyRecruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRecruitRepository extends JpaRepository<StudyRecruit, Long> {
    Page<StudyRecruit> findByStatusOrderByCreatedAtDesc(RecruitStatus status, Pageable pageable);
}
