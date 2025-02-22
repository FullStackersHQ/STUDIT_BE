package com.studit.backend.domain.room.service;

import com.studit.backend.domain.recruit.RecruitStatus;
import com.studit.backend.domain.recruit.StudyCategory;
import com.studit.backend.domain.recruit.entity.StudyRecruit;
import com.studit.backend.domain.recruit.entity.StudyRegister;
import com.studit.backend.domain.recruit.repository.StudyRecruitRepository;
import com.studit.backend.domain.room.MemberStatus;
import com.studit.backend.domain.room.RoomStatus;
import com.studit.backend.domain.room.dto.StudyRoomRequest;
import com.studit.backend.domain.room.dto.StudyRoomResponse;
import com.studit.backend.domain.room.entity.StudyMember;
import com.studit.backend.domain.room.entity.StudyRoom;
import com.studit.backend.domain.room.repository.StudyMemberRepository;
import com.studit.backend.domain.room.repository.StudyRoomRepository;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRoomService {

    private final StudyRecruitRepository studyRecruitRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;

    // 스터디룸 생성
    @Transactional
    public void createRoom(StudyRecruit recruit) {
        // 모집 상태 확인
        if (recruit.getStatus() != RecruitStatus.ACTIVE) {
            throw new IllegalStateException("이미 종료된 모집입니다.");
        }

        // 모집장을 제외한 가입자가 있어야 함
        List<StudyRegister> registerMembers = recruit.getStudyRegisters()
                .stream()
                .filter(register -> !register.getUser().equals(recruit.getLeader()))
                .toList();

        if (registerMembers.isEmpty()) {
            throw new IllegalStateException("가입자가 없어 스터디룸을 생성할 수 없습니다.");
        }

        // 스터디룸 생성
        StudyRoom studyRoom = StudyRoom.builder()
                .leader(recruit.getLeader()) // 모집의 리더를 스터디장으로 설정
                .title(recruit.getTitle())
                .description(recruit.getDescription())
                .tags(recruit.getTags())
                .category(recruit.getCategory())
                .goalTime(recruit.getGoalTime())
                .deposit(recruit.getDeposit())
                .studyStartAt(recruit.getRecruitEndAt()) // 모집 종료 시각 = 스터디 시작일
                .studyEndAt(recruit.getStudyEndAt())
                .status(RoomStatus.ACTIVE)
                .build();

        // 스터디멤버 생성
        List<StudyMember> studyMembers = new java.util.ArrayList<>(registerMembers.stream()
                .map(register -> StudyMember.builder()
                        .studyRoom(studyRoom)
                        .user(register.getUser())
                        .status(MemberStatus.MEMBER)
                        .deposit(recruit.getDeposit())
                        .build())
                .toList());

        // 모집장도 스터디장으로 추가
        studyMembers.add(
                StudyMember.builder()
                        .studyRoom(studyRoom)
                        .user(recruit.getLeader())
                        .status(MemberStatus.LEADER)
                        .deposit(recruit.getDeposit())
                        .build()
        );

        studyMemberRepository.saveAll(studyMembers);
        studyRoomRepository.save(studyRoom);
    }

    // 스터디룸 목록 조회
    public Page<StudyRoomResponse.Summary> getAllRooms(Pageable pageable) {
        Page<StudyRoom> rooms = studyRoomRepository.findByStatusOrderByStudyStartAtDesc(RoomStatus.ACTIVE, pageable);

        return rooms.map(studyRoom -> StudyRoomResponse.Summary.builder()
                .roomId(studyRoom.getId())
                .title(studyRoom.getTitle())
                .category(studyRoom.getCategory().name())
                .tags(Arrays.asList(studyRoom.getTags().split(",")))
                .goalTime(studyRoom.getGoalTime())
                .deposit(studyRoom.getDeposit())
                .studyStartAt(studyRoom.getStudyStartAt())
                .studyEndAt(studyRoom.getStudyEndAt())
                .currentMembers(studyRoom.getStudyMembers().size())
                .status(studyRoom.getStatus().name())
                .build());
    }

    // 스터디룸 목록 조회 (검색, 필터링 적용)
    public Page<StudyRoomResponse.Summary> getSearchRooms(
            String title, StudyCategory category,
            Integer minDeposit, Integer maxDeposit, Integer minGoalTime, Integer maxGoalTime,
            Pageable pageable
    ) {
        // null 값 방지
        int minDepositValue = (minDeposit != null) ? minDeposit : 0;
        int maxDepositValue = (maxDeposit != null) ? maxDeposit : Integer.MAX_VALUE;
        int minGoalTimeValue = (minGoalTime != null) ? minGoalTime : 0;
        int maxGoalTimeValue = (maxGoalTime != null) ? maxGoalTime : Integer.MAX_VALUE;

        Page<StudyRoom> searchRooms = studyRoomRepository.findByFilters(
                title, category, minDepositValue, maxDepositValue, minGoalTimeValue, maxGoalTimeValue, pageable);

        return searchRooms.map(studyRoom -> StudyRoomResponse.Summary.builder()
                .roomId(studyRoom.getId())
                .title(studyRoom.getTitle())
                .category(studyRoom.getCategory().name())
                .tags(Arrays.asList(studyRoom.getTags().split(",")))
                .goalTime(studyRoom.getGoalTime())
                .deposit(studyRoom.getDeposit())
                .studyStartAt(studyRoom.getStudyStartAt())
                .studyEndAt(studyRoom.getStudyEndAt())
                .currentMembers(studyRoom.getStudyMembers().size())
                .status(studyRoom.getStatus().name())
                .build());
    }

    // 스터디룸 상세 조회
    public StudyRoomResponse.Detail getDetailRoom(Long roomId) {
        StudyRoom studyRoom = studyRoomRepository.findById(roomId)
                .orElseThrow((() -> new EntityNotFoundException("Room not found")));

        return StudyRoomResponse.Detail.builder()
                .roomId(studyRoom.getId())
                .leaderId(studyRoom.getLeader().getId())
                .leaderNickname(studyRoom.getLeader().getNickname())
                .title(studyRoom.getTitle())
                .description(studyRoom.getDescription())
                .category(studyRoom.getCategory().name())
                .tags(Arrays.asList(studyRoom.getTags().split(",")))
                .goalTime(studyRoom.getGoalTime())
                .deposit(studyRoom.getDeposit())
                .studyStartAt(studyRoom.getStudyStartAt())
                .studyEndAt(studyRoom.getStudyEndAt())
                .currentMembers(studyRoom.getStudyMembers().size())
                .status(studyRoom.getStatus().name())
                .build();
    }

    // 스터디룸 수정 (제목, 설명, 태그 수정 가능)
    public void updateRoom(Long roomId, StudyRoomRequest.Update request, Long userId) {
        StudyRoom studyRoom = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("StudyRoom not found"));

        // 스터디장이 맞는지 확인
        if (!studyRoom.getLeader().getId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        studyRoom.update(request);
        studyRoomRepository.save(studyRoom);
    }

    // TODO: pointService 에서 구현될 수 있도록 수정 (실제 추가 + 로그)
    // 스터디룸 삭제
    @Transactional
    public void deleteRoom(Long roomId, Long userId) {
        StudyRoom studyRoom = studyRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("StudyRoom not found"));

        // 스터디장이 맞는지 확인
        if (!studyRoom.getLeader().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 스터디장의 포인트 환불
        leader.addPoint(studyRoom.getDeposit());

        // 스터디원들의 포인트 환불
        List<StudyMember> studyMembers = studyMemberRepository.findByStudyRoom(studyRoom);
        for (StudyMember member : studyMembers) {
            User user = member.getUser();
            user.addPoint(studyRoom.getDeposit());
        }

        studyRoomRepository.delete(studyRoom);
    }
}
