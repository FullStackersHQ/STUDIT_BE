package com.studit.backend.domain.room.entity;

import com.studit.backend.domain.room.MemberStatus;
import com.studit.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "study_members")
public class StudyMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; // 멤버 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private StudyRoom studyRoom; // 스터디룸

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MemberStatus status; // 멤버 상태 (LEADER, MEMBER)

    @Column(name = "deposit", nullable = false, updatable = false)
    private Integer deposit; // 멤버가 납부한 예치금
}
