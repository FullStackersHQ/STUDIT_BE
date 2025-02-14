package com.studit.backend.domain.recruit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonBackReference
    @JoinColumn(name = "recruit_id")
    private StudyRecruit studyRecruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RegisterStatus status = RegisterStatus.REGISTER;

    private LocalDateTime registerAt;
    @PrePersist
    public void prePersist() {
        if (this.registerAt == null) {
            this.registerAt = LocalDateTime.now(); // 현재 시간으로 기본값 설정
        }
    }
}
