package com.studit.backend.recruit.dto;

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
        private String status;
    }

    @Data
    public static class Detail {

    }

}
