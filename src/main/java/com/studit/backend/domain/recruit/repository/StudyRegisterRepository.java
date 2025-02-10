package com.studit.backend.recruit.repository;

import com.studit.backend.recruit.RegisterStatus;
import com.studit.backend.recruit.entity.StudyRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRegisterRepository extends JpaRepository<StudyRegister, Long> {
    int countByRecruitIdAndStatus(Long recruitId, RegisterStatus status);
}
