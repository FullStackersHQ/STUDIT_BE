package com.studit.backend.domain.user.controller;


import com.studit.backend.domain.recruit.entity.StudyRecruit;
import com.studit.backend.domain.study.entity.StudyRoom;
import com.studit.backend.domain.user.dto.StudyRecruitDto;
import com.studit.backend.domain.user.dto.StudyRoomDto;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import com.studit.backend.domain.user.service.UserService;
import com.studit.backend.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    //유저 닉네임 수정
    @PostMapping("/nickname")
    public void updateNickName(@RequestBody String userNickName ) {
        userService.checkUserNickName(userNickName);
    }

    //프로필 사진 수정
    @PostMapping("/profile-image")
    public void modifyProfImg(@RequestBody String userProfImg) {
    }
    /**
     * 일단 JPA세팅하고 제대로 동작하는지 -> 테스트 해보기 아직 스터디룸쪽은 완성안된것같음
     */
    //지난 평균 투두 리스트 달성률, 평균 스터디 비교
    @GetMapping("/study-status/stats")
    public void compareStudyCompletedFromLastMonth() {
        //아직 미완
    }
    /**
     * 스터디 현황조회 모든 (토큰 존재시)
     */
    @GetMapping("/study-status/total")
    public Page<StudyRoom> getStudyStatus(HttpServletRequest request,
                                          Pageable pageable) {
        // Bearer 제거 후 토큰만 추출
        String token = jwtTokenProvider.resolveToken(request);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId).get();
        // userId로 참여한 스터디 조회 (페이징 처리)
        return userService.getStudyStatusByUser(user, pageable);
    }
    /**
     * 스터디 현황조회 모든 (토큰 존재시) -test
     */
    @GetMapping("/study-status/total/test?page=0&size=10")
    public Page<StudyRoom> getStudyStatusTest(@RequestParam Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        // userId로 참여한 스터디 조회 (페이징 처리)
        return userService.getStudyStatusByUser(user, pageable);
    }
    /**
     * //참여 대기 -> 즉 모집 지원했고  -> 아직 모집이 시작되기전 -test
     */
    @GetMapping("/study-status/waiting")
    public Page<StudyRecruitDto> getStudiesWaiting(@RequestParam Long userId) {
        log.debug("🔍 Received userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        Page<StudyRecruit> studyRecruits  = userService.getStudyStatusByRecruit(user);
        //log.debug("✅ Retrieved studies: {}", studyRecruits.getTotalElements()); // 몇 개의 모집 정보를 가져왔는지 확인
        // ->> 이거 저장해서 전체 조회숫자에 줘야겠다.
        // StudyRecruit -> StudyRecruitDto로 변환
        Page<StudyRecruitDto> result = studyRecruits.map(StudyRecruitDto::new);
        return result;
    }
    /**
     * 테스트
     */
    //모집에서 참여로 들어가서 스터디룸 생성된
    //참여 중 스터디 목록 조회
    @GetMapping("/study-status/ongoing")
    public Page<StudyRoomDto> getOngoingStudies(@RequestParam Long userId) {
        //RegisterStatus -> COMPLETED 이고 ,StudyRoomStatus -> progress 이면되고 ,StutudyRoom의 LocalDateTime studyStartAt 이 오늘이상이면됌
        log.debug("🔍 Received userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        // 참여 중인 스터디 조회 (REGISTER + PROGRESS + 오늘 이후 시작)
        Page<StudyRoom> ongoingStudies = userService.getOngoingStudies(user);
        log.debug("✅ Ongoing studies count: {}", ongoingStudies.getTotalElements());
        return ongoingStudies.map(StudyRoomDto::fromEntity);
    }
    /**
     * 테스트
     */
    @GetMapping("/study-status/completed")
    public Page<StudyRoom> getStudiesCompleted(@RequestParam Long userId) {
         //StudyRoomStatus -> ENDED 이면되고 ,StutudyRoom의 LocalDateTime studyEndAt가 오늘을 지났면 됨.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
            return userService.getCompletedStudies(user);
    }

}



