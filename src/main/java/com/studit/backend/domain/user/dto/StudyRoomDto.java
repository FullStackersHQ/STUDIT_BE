package com.studit.backend.domain.user.dto;

import com.studit.backend.domain.study.entity.StudyRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private int maxMembers;
    private LocalDateTime studyStartAt;
    private LocalDateTime studyEndAt;
    private String status;

    // 엔티티 -> DTO 변환 메서드
    public static StudyRoomDto fromEntity(StudyRoom studyRoom) {
        return new StudyRoomDto(
                studyRoom.getRoomId(),
                studyRoom.getTitle(),
                studyRoom.getDescription(),
                studyRoom.getCategory().name(),
                studyRoom.getMaxMembers(),
                studyRoom.getStudyStartAt(),
                studyRoom.getStudyEndAt(),
                studyRoom.getStatus().name()
        );
    }
}