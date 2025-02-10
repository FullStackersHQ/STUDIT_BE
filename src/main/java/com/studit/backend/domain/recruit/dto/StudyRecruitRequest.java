package com.studit.backend.domain.recruit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studit.backend.domain.recruit.StudyCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class StudyRecruitRequest {

    @Data
    public static class Create {
        @NotBlank(message = "모집 제목은 필수 입력값입니다.")
        private String title;

        @NotBlank(message = "모집 설명은 필수 입력값입니다.")
        private String description;

        private List<String> tags;

        @NotNull(message = "카테고리는 필수 입력값입니다.")
        private StudyCategory category;

        @NotNull(message = "총 목표 시간은 필수 입력값입니다.")
        @Min(value = 1, message = "총 목표 시간은 1시간 이상이어야 합니다.")
        private Integer goalTime;

        @NotNull(message = "예치금은 필수 입력값입니다.")
        @Min(value = 0, message = "예치금은 0 이상이어야 합니다.")
        private Integer deposit;

        @NotNull(message = "최대 인원수는 필수 입력값입니다.")
        @Min(value = 1, message = "최대 인원수는 1명 이상이어야 합니다.")
        private Integer maxMembers;

        @NotNull(message = "스터디 종료일은 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH")
        private LocalDateTime studyEndAt;

        @NotNull(message = "모집 종료일은 필수 입력값입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH")
        private LocalDateTime recruitEndAt;
    }

    @Data
    public static class Search {
        private String title;

        private List<String> tags;

        private StudyCategory category;

        @Min(value = 0, message = "예치금은 0 이상이어야 합니다.")
        private int minDeposit;

        @Min(value = 0, message = "예치금은 0 이상이어야 합니다.")
        private int maxDeposit;

        @Min(value = 0, message = "목표 시간은 0 이상이어야 합니다.")
        private int minGoalTime;

        @Min(value = 0, message = "목표 시간은 0 이상이어야 합니다.")
        private int maxGoalTime;
    }
}
