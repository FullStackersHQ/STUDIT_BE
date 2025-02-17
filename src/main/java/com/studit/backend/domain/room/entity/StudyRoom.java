package com.studit.backend.domain.room.entity;

import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.room.RoomStatus;
import com.studit.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "study_rooms")
public class StudyRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id; // 스터디룸 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader; // 스터디장 ID

    @Column(name = "title", nullable = false, length = 255)
    private String title; // 스터디명

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description; // 스터디 설명

    @Column(name = "tags", length = 255)
    private String tags; // 태그 (쉼표로 구분)

    @Column(name = "category", nullable = false, updatable = false)
    private StudyCategory category; // 카테고리

    @Column(name = "goal_time", nullable = false, updatable = false)
    private Integer goalTime; // 총 목표시간

    @Column(name = "deposit", nullable = false, updatable = false)
    private Integer deposit; // 예치금

    @Column(name = "study_start_at", nullable = false, updatable = false)
    private LocalDateTime studyStartAt; // 스터디 시작일 (모집 종료 시각과 동일)

    @Column(name = "study_end_at", nullable = false, updatable = false)
    private LocalDateTime studyEndAt; // 스터디 종료일

    @Column(name = "notice_content", columnDefinition = "TEXT")
    private String noticeContent; // 공지 내용

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus status = RoomStatus.ACTIVE; // 스터디룸 상태 (ACTIVE, DELETED, COMPLETED)

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 최종 수정일
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyMember> members;
}
