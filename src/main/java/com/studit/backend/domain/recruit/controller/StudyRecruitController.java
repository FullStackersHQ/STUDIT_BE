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

        studyRecruitService.createRecruit(request, leaderId);

        return ResponseEntity.ok("모집글이 생성되었습니다.");
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
                title, category, minDeposit, maxDeposit, minGoalTime, maxGoalTime, pageable);

        return ResponseEntity.ok(searchRecruits);
    }

    // 스터디 모집글 상세 조회
    @GetMapping("/{recruitId}")
    public ResponseEntity<?> getDetailRecruit(@PathVariable Long recruitId) {
        StudyRecruitResponse.Detail recruit = studyRecruitService.getDetailRecruit(recruitId);

        return ResponseEntity.ok(recruit);
    }

    // 스터디 모집글 수정
    @PutMapping("/{recruitId}")
    public ResponseEntity<?> updateRecruit(
            @PathVariable Long recruitId,
            @RequestHeader("Authorization") String token,
            @Validated @RequestBody StudyRecruitRequest.Update request) {
        // JWT 에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        studyRecruitService.updateRecruit(recruitId, request, userId);

        return ResponseEntity.ok("모집글이 수정되었습니다.");
    }

    // 스터디 모집글 삭제
    @DeleteMapping("/{recruitId}")
    public ResponseEntity<?> deleteRecruit(
            @PathVariable Long recruitId,
            @RequestHeader("Authorization") String token) {
        // JWT 에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        studyRecruitService.deleteRecruit(recruitId, userId);

        return ResponseEntity.ok("모집글이 삭제되었습니다.");
    }

    // 스터디 가입
    @PostMapping("/{recruitId}/registers")
    public ResponseEntity<?> studyRegister(
            @PathVariable Long recruitId,
            @RequestHeader("Authorization") String token) {
        // JWT 에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        studyRecruitService.studyRegister(recruitId, userId);

        return ResponseEntity.ok("스터디에 가입되셨습니다.");
    }

    // 스터디 가입 철회
    @DeleteMapping("/{recruitId}/registers")
    public ResponseEntity<?> withdrawRegister(
            @PathVariable Long recruitId,
            @RequestHeader("Authorization") String token) {
        // JWT 에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        studyRecruitService.withdrawRegister(recruitId, userId);

        return ResponseEntity.ok("스터디 가입이 철회되었습니다.");
    }
}