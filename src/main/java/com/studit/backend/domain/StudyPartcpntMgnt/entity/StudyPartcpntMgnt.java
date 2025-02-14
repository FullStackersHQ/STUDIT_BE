package com.studit.backend.domain.StudyPartcpntMgnt.entity;


import com.studit.backend.domain.StudyPartcpntMgnt.PartcpntStatus;
import com.studit.backend.domain.StudyPartcpntMgnt.StudyStatus;
import com.studit.backend.domain.study.entity.StudyRoom;
import com.studit.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "study_partcpnt_mgnt")
public class StudyPartcpntMgnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private StudyRoom room;

    @Enumerated(EnumType.STRING)
    private PartcpntStatus user_code;

    // 참여일시
    @CreationTimestamp
    @Column(nullable = false, updatable = false)  // 수정 불가
    private LocalDateTime joinAt;

    //스터디 생성당시 예치금 -> 예치금/모집원수
    private int joinPoint;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyCompleteStatus;

    // 생성일시
    @CreationTimestamp
    @Column(nullable = false, updatable = false)  // 수정 불가
    private LocalDateTime createdAt;

}
