package com.studit.backend.domain.StudyPartcpntMgnt.repository;

import com.studit.backend.domain.StudyPartcpntMgnt.entity.StudyPartcpntMgnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyPartcpntMgntRepository extends JpaRepository<StudyPartcpntMgnt, Long> {
}
