package com.studit.backend.recruit.service;

import com.studit.backend.recruit.RecruitStatus;
import com.studit.backend.recruit.RegisterStatus;
import com.studit.backend.recruit.dto.StudyRecruitRequest;
import com.studit.backend.recruit.dto.StudyRecruitResponse;
import com.studit.backend.recruit.entity.StudyRecruit;
import com.studit.backend.recruit.repository.StudyRecruitRepository;
import com.studit.backend.recruit.repository.StudyRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class StudyRecruitService {

    private final StudyRecruitRepository studyRecruitRepository;
    private final StudyRegisterRepository studyRegisterRepository;

    // 스터디 모집 생성
    @Transactional
    public Long createRecruit(StudyRecruitRequest.Create request, Long leaderId) {

        // TODO: User 엔티티가 만들어져야함
        // 모집자 정보 가져오기
        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // TODO: User 엔티티 필드에 point 가 추가 되어야함
        // 모집자가 가진 포인트가 예치금보다 적으면 예외 발생
        if (leader.getPoint() < request.getDeposit()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        // 모집글 생성
        StudyRecruit studyRecruit = StudyRecruit.builder()
                .leader(leader)
                .title(request.getTitle())
                .description(request.getDescription())
                .tags(String.join(",", request.getTags()))
                .category(request.getCategory())
                .goalTime(request.getGoalTime())
                .deposit(request.getDeposit())
                .maxMembers(request.getMaxMembers())
                .studyStartAt(request.getRecruitEndAt())
                .studyEndAt(request.getStudyEndAt())
                .recruitEndAt(request.getRecruitEndAt())
                .build();

        // TODO: User 엔티티에 해당 메서드 추가
        // public void deductPoint(Integer deposit) {
        //    if (this.point < deposit) {
        //        throw new IllegalArgumentException("포인트가 부족합니다.");
        //    }
        //    this.point -= deposit;
        //}
        // 모집자의 포인트 차감
        leader.deductPoint(request.getDeposit());

        return this.studyRecruitRepository.save(studyRecruit).getId();
    }

    // 스터디 모집 목록 조회
    public Page<StudyRecruitResponse.Summary> getAllRecruits(Pageable pageable) {
        Page<StudyRecruit> recruits = this.studyRecruitRepository.findByStatusOrderByCreatedAtDesc(RecruitStatus.ACTIVE, pageable);

        return recruits.map(studyRecruit -> StudyRecruitResponse.Summary.builder()
                .recruitId(studyRecruit.getId())
                .title(studyRecruit.getTitle())
                .category(studyRecruit.getCategory().name())
                .tags(Arrays.asList(studyRecruit.getTags().split(",")))
                .goalTime(studyRecruit.getGoalTime())
                .deposit(studyRecruit.getDeposit())
                .studyStartAt(studyRecruit.getStudyStartAt())
                .studyEndAt(studyRecruit.getStudyEndAt())
                .recruitEndAt(studyRecruit.getRecruitEndAt())
                .currentMembers(this.studyRegisterRepository.countByRecruitIdAndStatus(studyRecruit.getId(), RegisterStatus.REGISTER))
                .maxMembers(studyRecruit.getMaxMembers())
                .status(studyRecruit.getStatus().name())
                .build());
    }

    // 스터디 모집 목록 조회 (검색, 필터링 적용)
    public Page<StudyRecruitResponse.Summary> getSearchRecruits(StudyRecruitRequest.Search request) {
        if (request.getMaxDeposit() < request.getMinDeposit()) {
            throw new IllegalArgumentException("최대 예치금은 최소 예치금보다 커야 합니다.");
        }

        if (request.getMaxGoalTime() < request.getMinGoalTime()) {
            throw new IllegalArgumentException("최대 목표 시간은 최소 목표 시간보다 커야 합니다.");
        }
    }
}
