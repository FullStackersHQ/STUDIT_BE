package com.studit.backend.domain.room.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

public class StudyRoomRequest {
    @Data
    public static class Update {
        @NotBlank(message = "스터디룸 제목은 필수 입력값입니다.")
        private String title;

        @NotBlank(message = "스터디룸 설명은 필수 입력값입니다.")
        private String description;

        private List<String> tags;
    }

    @Data
    public static class NoticeContent {
        private String content;
    }
}
