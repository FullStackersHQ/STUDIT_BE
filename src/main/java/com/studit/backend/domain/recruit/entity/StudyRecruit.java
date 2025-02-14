package com.studit.backend.domain.recruit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studit.backend.domain.recruit.RecruitStatus;
import com.studit.backend.domain.recruit.StudyCategory;
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
    @JoinColumn(name = "leader_id")
    private User leader; // 모집자 ID (스터디장)

    private String title; // 모집제목
    private String description; // 모집설명
    private String tags; // 태그(검색용)

    @Enumerated(EnumType.STRING)
    private StudyCategory category; // 카테고리 (코딩, 공무원 등)

    private Integer goalTime; // 총 목표시간
    private Integer deposit; // 예치금
    private Integer maxMembers; // 최대 인원수

    private LocalDateTime studyStartAt; // 스터디 시작일
    private LocalDateTime studyEndAt; // 스터디 종료일

    private LocalDateTime recruitStartAt; // 모집 시작일
    @PrePersist
    public void prePersist() {
        if (this.recruitStartAt == null) {
            this.recruitStartAt = LocalDateTime.now(); // 현재 시간으로 기본값 설정
        }
    }

    private LocalDateTime recruitEndAt; // 모집 종료일

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RecruitStatus status = RecruitStatus.ACTIVE; // 모집 상태 (ACTIVE, DELETED, COMPLETED 등)

    private LocalDateTime updatedAt; // 최종수정일

    @OneToMany(mappedBy = "studyRecruit", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<StudyRegister> studyRegisters; // 가입자 리스트
}
