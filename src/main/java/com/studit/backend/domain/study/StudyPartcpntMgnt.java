//package com.studit.backend.domain.study;
//
//
//import com.studit.backend.domain.user.entity.User;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@NoArgsConstructor
//public class StudyPartcpntMgnt {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    private String studyCode;
//    private String studyName;
//    private String studyStatus;
//
//    //스터디룸과 연관관계 추가
//
//
//
//}
