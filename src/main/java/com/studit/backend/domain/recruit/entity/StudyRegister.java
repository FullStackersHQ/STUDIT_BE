package com.studit.backend.domain.recruit.entity;

import com.studit.backend.domain.recruit.RegisterStatus;
import com.studit.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "study_registers")
public class StudyRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id", nullable = false)
    private StudyRecruit studyRecruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegisterStatus status = RegisterStatus.REGISTER;

    @Column(name = "register_at", nullable = false, updatable = false)
    private LocalDateTime registerAt;
    @PrePersist
    public void prePersist() {
        if (this.registerAt == null) {
            this.registerAt = LocalDateTime.now(); // 현재 시간으로 기본값 설정
        }
    }
}
