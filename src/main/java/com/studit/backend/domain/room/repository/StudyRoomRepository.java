package com.studit.backend.domain.room.repository;

import com.studit.backend.domain.room.RoomStatus;
import com.studit.backend.domain.room.entity.StudyRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    Page<StudyRoom> findByStatusOrderByStudyStartAtDesc(RoomStatus status, Pageable pageable);
}
