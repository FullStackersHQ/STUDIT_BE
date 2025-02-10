package com.studit.backend.recruit.controller;

import com.studit.backend.recruit.dto.StudyRecruitRequest;
import com.studit.backend.recruit.dto.StudyRecruitResponse;
import com.studit.backend.recruit.service.StudyRecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruits")
public class StudyRecruitController {

    private final StudyRecruitService studyRecruitService;

    // TODO: leaderId는 토큰에서 추출
    // 스터디 모집 생성
    @PostMapping
    public ResponseEntity<?> createRecruit(@Validated @RequestBody StudyRecruitRequest.Create request,
                                           @RequestParam Long leaderId) {

        return ResponseEntity.ok(this.studyRecruitService.createRecruit(request, leaderId));
    }

    // 스터디 모집 목록 조회
    @GetMapping
    public ResponseEntity<Page<StudyRecruitResponse.Summary>> getAllRecruits(@RequestParam(defaultValue = "0") int page) {
        // 한 페이지당 5개
        int pageSize = 5;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StudyRecruitResponse.Summary> recruits = this.studyRecruitService.getAllRecruits(pageable);

        return ResponseEntity.ok(recruits);
    }

    // 스터디 모집 목록 조회 (검색, 필터링 적용)
    @GetMapping
    public ResponseEntity<?> getSearchRecruits() {

        return null;
    }
}