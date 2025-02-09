package com.studit.backend.domain.user.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Getter
@ToString
@NoArgsConstructor
public class User {

    @Id
    private Long id;

    private String nickname;
    private String profileImage;
    private double point;  // 성진님 요청으로
    private String email;  // 가져올 순 있음

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    public User(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImageUrl;
    }
}
