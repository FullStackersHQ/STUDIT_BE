package com.studit.backend.domain.room.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class StudyRoomResponse {

    @Builder
    @Data
    public static class Summary {
        private Long roomId;
        private String title;
        private String category;
        private List<String> tags;
        private int goalTime;
        private int deposit;
        private LocalDateTime studyStartAt;
        private LocalDateTime studyEndAt;
        private int currentMembers;
        private String status;
    }

    @Builder
    @Data
    public static class Detail {
        private Long roomId;
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
        private int currentMembers;
        private String status;
    }
}
