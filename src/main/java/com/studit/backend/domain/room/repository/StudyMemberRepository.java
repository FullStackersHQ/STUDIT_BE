package com.studit.backend.domain.room.repository;

import com.studit.backend.domain.room.entity.StudyMember;
import com.studit.backend.domain.room.entity.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findByStudyRoom(StudyRoom studyRoom);
}
