package com.studit.backend.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studit.backend.domain.StudyPartcpntMgnt.entity.StudyPartcpntMgnt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 🔥 Hibernate 프록시 무시
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 전략 지정
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(unique = true)
    private Long kakaoId;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int point;

    @Column(nullable = true)
    private String email;  //필요없으면 삭제.. 프론트분들께 오늘밤 여쭤보기


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10) // 적절한 길이 지정
    private Role role; // 사용자 권한 (USER, ADMIN)
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
    // 생성일시
    @CreationTimestamp
    @Column(nullable = false, updatable = false)  // 수정 불가
    private LocalDateTime createdAt;

    // 수정일시
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


//    // 포인트로그(1:N)
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<pointLog.PointLog> pointLogs;

    // 스터디참여자관리(1:N)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StudyPartcpntMgnt> studyPartcpntMgntList;

    public User(Long kakaoId, String nickname, String profileImageUrl, Role role) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImageUrl;
        this.role = role;
    }

    // 예치금만큼 차감 후 남은 포인트로 수정
    public void deductPoint(int deposit) {
        this.point -= deposit;
    }

}
