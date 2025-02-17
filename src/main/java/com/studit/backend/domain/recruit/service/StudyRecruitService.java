package com.studit.backend.domain.recruit.service;

import com.studit.backend.domain.recruit.RecruitStatus;
import com.studit.backend.domain.recruit.RegisterStatus;
import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.recruit.dto.StudyRecruitRequest;
import com.studit.backend.domain.recruit.dto.StudyRecruitResponse;
import com.studit.backend.domain.recruit.entity.StudyRecruit;
import com.studit.backend.domain.recruit.entity.StudyRegister;
import com.studit.backend.domain.recruit.repository.StudyRecruitRepository;
import com.studit.backend.domain.recruit.repository.StudyRegisterRepository;
import com.studit.backend.domain.room.service.StudyRoomService;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRecruitService {

    private final StudyRecruitRepository studyRecruitRepository;
    private final StudyRegisterRepository studyRegisterRepository;
    private final StudyRoomService studyRoomService;
    private final UserRepository userRepository;
    private final TaskScheduler taskScheduler;

    // 모집 종료 시 스터디룸 자동 생성 예약
    @Transactional
    public void scheduleCreateRoom(Long recruitId) {
        StudyRecruit recruit = studyRecruitRepository.findById(recruitId)
                .orElseThrow(() -> new EntityNotFoundException("Recruit not found"));

        LocalDateTime recruitEndAt = recruit.getRecruitEndAt();

        // 모집 종료 시간이 현재보다 이전이면 실행하지 않음
        if (recruitEndAt.isBefore(LocalDateTime.now())) {
            return;
        }

        // 모집 종료 시각에 스터디룸 생성 예약
        taskScheduler.schedule(() -> closeRecruitAndCreateRoom(recruit.getId()),
                java.util.Date.from(recruitEndAt.atZone(ZoneId.systemDefault()).toInstant()));
    }

    // 모집이 종료되었을 때 실행되는 메서드
    @Transactional
    public void closeRecruitAndCreateRoom(Long recruitId) {
        StudyRecruit recruit = studyRecruitRepository.findById(recruitId)
                .orElseThrow(() -> new EntityNotFoundException("Recruit not found"));

        // 모집 상태를 완료로 변경
        recruit.setStatus(RecruitStatus.COMPLETED);
        studyRecruitRepository.save(recruit);

        // 모집이 진행중인지 다시 확인 (변경 가능성 있음)
        if (recruit.getStatus() == RecruitStatus.ACTIVE) {
            studyRoomService.createRoom(recruit);
        }
    }

    // TODO: pointService 에서 구현될 수 있도록 수정 (실제 차감 + 로그)
    // 스터디 모집글 생성
    @Transactional
    public Long createRecruit(StudyRecruitRequest.Create request, Long leaderId) {

        // 모집자 정보 가져오기
        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 모집자가 가진 포인트가 예치금보다 적으면 예외 발생
        if (leader.getPoint() < request.getDeposit()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        // 모집자의 포인트 차감
        leader.deductPoint(request.getDeposit());

        // 모집글 생성
        StudyRecruit recruit = StudyRecruit.builder()
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

        Long recruitId = studyRecruitRepository.save(recruit).getId();

        // 모집장을 자동 가입 처리
        StudyRegister register = StudyRegister.builder()
                .studyRecruit(recruit)
                .user(leader)
                .status(RegisterStatus.REGISTER)
                .build();

        studyRegisterRepository.save(register);

        return recruitId;
    }

    // 스터디 모집글 목록 조회
    public Page<StudyRecruitResponse.Summary> getAllRecruits(Pageable pageable) {
        Page<StudyRecruit> recruits = studyRecruitRepository.findByStatusOrderByRecruitStartAtDesc(RecruitStatus.ACTIVE, pageable);

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
                .currentMembers(studyRegisterRepository.countByStudyRecruitAndStatus(studyRecruit, RegisterStatus.REGISTER))
                .maxMembers(studyRecruit.getMaxMembers())
                .status(studyRecruit.getStatus().name())
                .build());
    }

    // 스터디 모집글 목록 조회 (검색, 필터링 적용)
    public Page<StudyRecruitResponse.Summary> getSearchRecruits(
            String title, StudyCategory category,
            Integer minDeposit, Integer maxDeposit, Integer minGoalTime, Integer maxGoalTime,
            Pageable pageable
    ) {
        // null 값 방지
        int minDepositValue = (minDeposit != null) ? minDeposit : 0;
        int maxDepositValue = (maxDeposit != null) ? maxDeposit : Integer.MAX_VALUE;
        int minGoalTimeValue = (minGoalTime != null) ? minGoalTime : 0;
        int maxGoalTimeValue = (maxGoalTime != null) ? maxGoalTime : Integer.MAX_VALUE;

        Page<StudyRecruit> searchRecruits = studyRecruitRepository.findByFilters(
                title, category, minDepositValue, maxDepositValue, minGoalTimeValue, maxGoalTimeValue, pageable);

        return searchRecruits.map(studyRecruit -> StudyRecruitResponse.Summary.builder()
                .recruitId(studyRecruit.getId())
                .title(studyRecruit.getTitle())
                .category(studyRecruit.getCategory().name())
                .tags(Arrays.asList(studyRecruit.getTags().split(",")))
                .goalTime(studyRecruit.getGoalTime())
                .deposit(studyRecruit.getDeposit())
                .studyStartAt(studyRecruit.getStudyStartAt())
                .studyEndAt(studyRecruit.getStudyEndAt())
                .recruitEndAt(studyRecruit.getRecruitEndAt())
                .currentMembers(studyRegisterRepository.countByStudyRecruitAndStatus(studyRecruit, RegisterStatus.REGISTER))
                .maxMembers(studyRecruit.getMaxMembers())
                .status(studyRecruit.getStatus().name())
                .build());
    }

    // 스터디 모집글 상세 조회
    public StudyRecruitResponse.Detail getDetailRecruit(Long recruitId) {
        StudyRecruit recruit = studyRecruitRepository.findById(recruitId)
                .orElseThrow((() -> new EntityNotFoundException("Recruit not found")));

        return StudyRecruitResponse.Detail.builder()
                .recruitId(recruit.getId())
                .leaderId(recruit.getLeader().getId())
                .leaderNickname(recruit.getLeader().getNickname())
                .title(recruit.getTitle())
                .description(recruit.getDescription())
                .category(recruit.getCategory().name())
                .tags(Arrays.asList(recruit.getTags().split(",")))
                .goalTime(recruit.getGoalTime())
                .deposit(recruit.getDeposit())
                .studyStartAt(recruit.getStudyStartAt())
                .studyEndAt(recruit.getStudyEndAt())
                .recruitEndAt(recruit.getRecruitEndAt())
                .currentMembers(studyRegisterRepository.countByStudyRecruitAndStatus(recruit, RegisterStatus.REGISTER))
                .maxMembers(recruit.getMaxMembers())
                .status(recruit.getStatus().name())
                .build();
    }

    // 스터디 모집글 수정
    public void updateRecruit(Long recruitId, StudyRecruitRequest.Update request, Long userId) {
        StudyRecruit recruit = studyRecruitRepository.findById(recruitId)
                .orElseThrow(() -> new EntityNotFoundException("Recruit not found"));

        // 모집장이 맞는지 확인
        if (!recruit.getLeader().getId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        recruit.update(request);
        studyRecruitRepository.save(recruit);
    }

    // TODO: pointService 에서 구현될 수 있도록 수정 (실제 추가 + 로그)
    // 스터디 모집글 삭제
    @Transactional
    public void deleteRecruit(Long recruitId, Long userId) {
        StudyRecruit recruit = studyRecruitRepository.findById(recruitId)
                .orElseThrow(() -> new EntityNotFoundException("Recruit not found"));

        // 모집장이 맞는지 확인
        if (!recruit.getLeader().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 모집자의 포인트 환불
        leader.addPoint(recruit.getDeposit());

        // 가입자들의 포인트 환불
        List<StudyRegister> studyRegisters = studyRegisterRepository.findByStudyRecruit(recruit);
        for (StudyRegister register : studyRegisters) {
            User user = register.getUser();
            user.addPoint(recruit.getDeposit());
        }

        // 모집자의 예치금 환불 처리 (pointService 사용)
//        User leader = recruit.getLeader();
//        pointService.refundDeposit(leader.getId(), recruit.getDeposit());

        // 가입자들의 예치금 환불 처리 (pointService 사용)
//        List<StudyRegister> studyRegisters = studyRegisterRepository.findByStudyRecruit(recruit);
//        for (StudyRegister register : studyRegisters) {
//            User user = register.getUser();
//            pointService.refundDeposit(user.getId(), recruit.getDeposit());
//        }

        studyRecruitRepository.delete(recruit);
    }

    // TODO: pointService 에서 구현될 수 있도록 수정 (실제 차감 + 로그)
    // 스터디 가입
    @Transactional
    public void studyRegister(Long recruitId, Long userId) {
        // 모집글 조회
        StudyRecruit recruit = studyRecruitRepository.findById(recruitId)
                .orElseThrow(() -> new EntityNotFoundException("Recruit not found"));

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 가입자가 가진 포인트가 예치금보다 적으면 예외 발생
        if (user.getPoint() < recruit.getDeposit()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        // 가입자의 포인트 차감
        user.deductPoint(recruit.getDeposit());

        // 최대 인원수보다 더 가입할 수 없음
        int currentMembers = studyRegisterRepository.countByStudyRecruitAndStatus(recruit, RegisterStatus.REGISTER);
        if (currentMembers >= recruit.getMaxMembers()) {
            throw new IllegalStateException("최대 인원 수를 초과하여 가입할 수 없습니다.");
        }

        // 모집장은 자신의 모집에 가입할 수 없음
        if (recruit.getLeader().equals(user)) {
            throw new IllegalStateException("모집장은 자신의 모집에 가입할 수 없습니다.");
        }

        // 중복 가입 방지
        boolean alreadyRegistered = studyRegisterRepository.existsByStudyRecruitAndUser(recruit, user);
        if (alreadyRegistered) {
            throw new IllegalStateException("이미 가입한 스터디입니다.");
        }

        // 스터디 가입 정보 저장
        StudyRegister studyRegister = StudyRegister.builder()
                .studyRecruit(recruit)
                .user(user)
                .build();

        studyRegisterRepository.save(studyRegister);
    }

    // TODO: pointService 에서 구현될 수 있도록 수정 (실제 추가 + 로그)
    // 스터디 가입 철회
    @Transactional
    public void withdrawRegister(Long recruitId, Long userId) {
        // 가입 정보 조회
        StudyRegister studyRegister = studyRegisterRepository.findByStudyRecruitIdAndUserId(recruitId, userId)
                .orElseThrow(() -> new IllegalStateException("가입한 스터디가 아닙니다."));

        StudyRecruit recruit = studyRegister.getStudyRecruit();
        User user = studyRegister.getUser();

        // 모집 기간이 끝났다면 가입 철회 불가
        if (LocalDateTime.now().isAfter(recruit.getRecruitEndAt())) {
            throw new IllegalStateException("모집이 종료되어 가입을 철회할 수 없습니다.");
        }

        // 모집 기간이 끝나지 않았다면 포인트 환불
        user.addPoint(recruit.getDeposit());

        studyRegisterRepository.delete(studyRegister);
    }
}
