package com.studit.backend.domain.recruit.repository;

import com.studit.backend.domain.recruit.RegisterStatus;
import com.studit.backend.domain.recruit.entity.StudyRecruit;
import com.studit.backend.domain.recruit.entity.StudyRegister;
import com.studit.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRegisterRepository extends JpaRepository<StudyRegister, Long> {
    int countByStudyRecruitAndStatus(StudyRecruit studyRecruit, RegisterStatus status);
    List<StudyRegister> findByStudyRecruit(StudyRecruit studyRecruit);
    Optional<StudyRegister> findByStudyRecruitIdAndUserId(Long recruitId, Long userId);
}
