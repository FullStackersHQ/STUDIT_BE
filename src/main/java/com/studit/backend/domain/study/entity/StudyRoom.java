package com.studit.backend.domain.study.entity;

import com.studit.backend.domain.StudyPartcpntMgnt.entity.StudyPartcpntMgnt;
import com.studit.backend.domain.study.StudyRoomStatus;
import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.studit.backend.domain.study.StudyRoomStatus.*;


@Entity
@Getter
@Setter
public class StudyRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

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

    @Enumerated(EnumType.STRING)
    private StudyRoomStatus status = PROGRESS; // 스터디 상태 (progress(진행중), ended(종료))

    // 수정일시
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
