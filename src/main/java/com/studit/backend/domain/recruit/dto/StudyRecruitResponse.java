package com.studit.backend.domain.recruit.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class StudyRecruitResponse {

    @Builder
    @Data
    public static class Summary {
        private Long recruitId;
        private String title;
        private String category;
        private List<String> tags;
        private int goalTime;
        private int deposit;
        private LocalDateTime studyStartAt;
        private LocalDateTime studyEndAt;
        private LocalDateTime recruitEndAt;
        private int currentMembers;
        private int maxMembers;
        private String status;}

    @Builder
    @Data
    public static class Detail {
        private Long recruitId;
        private Long leaderId;
        private String leaderNickname;
        private String title;
        private String description;
        private String category;
        private List<String> tags;
        private int goalTime;
        private int deposit;
        private LocalDateTime studyStartAt;
        private LocalDateTime studyEndAt;
        private LocalDateTime recruitEndAt;
        private int currentMembers;
        private int maxMembers;
        private String status;}}