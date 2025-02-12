package com.studit.backend.domain.recruit.controller;

import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.recruit.dto.StudyRecruitRequest;
import com.studit.backend.domain.recruit.dto.StudyRecruitResponse;
import com.studit.backend.domain.recruit.service.StudyRecruitService;
import com.studit.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruits")
public class StudyRecruitController {

    private final StudyRecruitService studyRecruitService;
    private final JwtTokenProvider jwtTokenProvider;

    // 스터디 모집글 생성
    @PostMapping
    public ResponseEntity<?> createRecruit(
            @RequestHeader("Authorization") String token,
            @Validated @RequestBody StudyRecruitRequest.Create request
    ) {
        // JWT 에서 userId 추출
        Long leaderId = jwtTokenProvider.getUserIdFromToken(token);

        return ResponseEntity.ok(studyRecruitService.createRecruit(request, leaderId));
    }

    // 스터디 모집글 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllRecruits(@RequestParam(defaultValue = "0") int page) {
        // 한 페이지당 5개
        int pageSize = 5;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StudyRecruitResponse.Summary> recruits = studyRecruitService.getAllRecruits(pageable);

        return ResponseEntity.ok(recruits);
    }

    // 스터디 모집글 목록 조회 (검색, 필터링 적용)
    @GetMapping("/search")
    public ResponseEntity<?> getSearchRecruits(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) StudyCategory category,
            @RequestParam(required = false) Integer minDeposit,
            @RequestParam(required = false) Integer maxDeposit,
            @RequestParam(required = false) Integer minGoalTime,
            @RequestParam(required = false) Integer maxGoalTime,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 한 페이지당 5개
        int pageSize = 5;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<StudyRecruitResponse.Summary> searchRecruits = studyRecruitService.getSearchRecruits(
                title, tags, category, minDeposit, maxDeposit, minGoalTime, maxGoalTime, pageable);

        return ResponseEntity.ok(searchRecruits);
    }

    // 스터디 모집글 상세 조회
    @GetMapping("/{recruitId}")
    public ResponseEntity<?> getDetailRecruit(@PathVariable Long recruitId) {
        StudyRecruitResponse.Detail recruit = studyRecruitService.getDetailRecruit(recruitId);

        return ResponseEntity.ok(recruit);
    }

    // TODO: 제목, 설명, 카테고리, 태그, 최대 인원수 수정 가능
    // 스터디 모집글 수정
    @PutMapping("/{recruitId}")
    public ResponseEntity<?> updateRecruit() {

        return null;
    }

    // TODO: 모집자와 가입자 포인트 환불
    // 스터디 모집글 삭제
    @DeleteMapping("/{recruitId}")
    public ResponseEntity<?> deleteRecruit() {

        return null;
    }

    // TODO: 가입시 포인트 차감
    // 스터디 가입
    @PostMapping("/{recruitId}/registers")
    public ResponseEntity<?> studyRegister() {

        return null;
    }

    // TODO: 철회시 포인트 환불
    // 스터디 가입 철회
    @DeleteMapping("/{recruitId}/registers")
    public ResponseEntity<?> withdrawRegister() {
        return null;
    }
}