package com.studit.backend.domain.room.service;

import com.studit.backend.domain.recruit.RecruitStatus;
import com.studit.backend.domain.recruit.entity.StudyRecruit;
import com.studit.backend.domain.recruit.entity.StudyRegister;
import com.studit.backend.domain.recruit.repository.StudyRecruitRepository;
import com.studit.backend.domain.room.MemberStatus;
import com.studit.backend.domain.room.RoomStatus;
import com.studit.backend.domain.room.entity.StudyMember;
import com.studit.backend.domain.room.entity.StudyRoom;
import com.studit.backend.domain.room.repository.StudyMemberRepository;
import com.studit.backend.domain.room.repository.StudyRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRoomService {

    private final StudyRecruitRepository studyRecruitRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyMemberRepository studyMemberRepository;

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
}
