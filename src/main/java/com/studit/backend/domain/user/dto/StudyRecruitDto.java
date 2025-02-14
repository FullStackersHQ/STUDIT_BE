package com.studit.backend.domain.user.dto;

import com.studit.backend.domain.recruit.entity.StudyRecruit;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudyRecruitDto {
    private Long id;
    private String title;
    private String description;
    private String tags;
    private String category;
    private LocalDateTime recruitStartAt;
    private LocalDateTime recruitEndAt;
    private String status;

    public StudyRecruitDto(StudyRecruit studyRecruit) {
        this.id = studyRecruit.getId();
        this.title = studyRecruit.getTitle();
        this.description = studyRecruit.getDescription();
        this.tags = studyRecruit.getTags();
        this.category = String.valueOf(studyRecruit.getCategory());
        this.recruitStartAt = studyRecruit.getRecruitStartAt();
        this.recruitEndAt = studyRecruit.getRecruitEndAt();
        this.status = String.valueOf(studyRecruit.getStatus());
    }
}