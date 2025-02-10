package com.studit.backend.domain.user.entity;

//import com.studit.backend.domain.study.StudyPartcpntMgnt;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;


@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /*
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Id 와 관련해서 -> 우리 데이터베이스에서 ID를 부여하는것이 나을까?
    카카오톡 id를 그대로가져와서 쓰는게 맞을까?? 카카오톡 ID도 고유하긴함.대신 ID가길어서
    bitint로 들어가긴하는 것 같음.
    */

    @Id
    @Column(nullable = false, unique = true)
    private long id;


    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int point;

    @Column(nullable = true)
    private String email;  //필요없으면 삭제.. 프론트분들께 오늘밤 여쭤보기


    // 생성일시
    @CreationTimestamp
    @Column(nullable = false, updatable = false)  // 수정 불가
    private Date createdAt;

    // 수정일시
    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;


//    // 포인트로그(1:N)
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<pointLog.PointLog> pointLogs;

//    // 스터디참여자관리(1:N)
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<StudyPartcpntMgnt> studyPartcpntMgntList;

    public User(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImageUrl;
    }

}
