package com.studit.backend.domain.recruit.entity;

import com.studit.backend.domain.recruit.RecruitStatus;
import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.recruit.dto.StudyRecruitRequest;
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
@Table(name = "study_recruits")
public class StudyRecruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id; // 모집 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader; // 모집자 ID (스터디장)

    @Column(name = "title", nullable = false, length = 255)
    private String title; // 모집제목

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description; // 모집설명

    @Column(name = "tags", length = 255)
    private String tags; // 태그(검색용)

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private StudyCategory category; // 카테고리 (코딩, 공무원 등)

    @Column(name = "goal_time", nullable = false, updatable = false)
    private Integer goalTime; // 총 목표시간

    @Column(name = "deposit", nullable = false, updatable = false)
    private Integer deposit; // 예치금

    @Column(name = "max_members", nullable = false)
    private Integer maxMembers; // 최대 인원수

    @Column(name = "study_start_at", nullable = false, updatable = false)
    private LocalDateTime studyStartAt; // 스터디 시작일

    @Column(name = "study_end_at", nullable = false, updatable = false)
    private LocalDateTime studyEndAt; // 스터디 종료일

    @Column(name = "recruit_start_at", nullable = false, updatable = false)
    private LocalDateTime recruitStartAt; // 모집 시작일
    @PrePersist
    public void prePersist() {
        if (this.recruitStartAt == null) {
            this.recruitStartAt = LocalDateTime.now(); // 현재 시간으로 기본값 설정
        }
    }

    @Column(name = "recruit_end_at", nullable = false, updatable = false)
    private LocalDateTime recruitEndAt; // 모집 종료일

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RecruitStatus status = RecruitStatus.ACTIVE; // 모집 상태 (ACTIVE, DELETED, COMPLETED 등)

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 최종수정일
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "studyRecruit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyRegister> studyRegisters; // 가입자 리스트

    // 수정할 데이터만 업데이트하는 메서드
    public void update(StudyRecruitRequest.Update request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.tags = String.join(",", request.getTags());
        this.category = request.getCategory();
        this.maxMembers = request.getMaxMembers();
    }
}
