package com.studit.backend.domain.user.service;

import com.studit.backend.domain.recruit.RegisterStatus;
import com.studit.backend.domain.recruit.entity.StudyRecruit;
import com.studit.backend.domain.recruit.repository.StudyRecruitRepository;
import com.studit.backend.domain.recruit.repository.StudyRegisterRepository;
import com.studit.backend.domain.study.StudyRoomStatus;
import com.studit.backend.domain.study.entity.StudyRoom;
import com.studit.backend.domain.study.repository.StudyRoomRepository;
import com.studit.backend.domain.user.entity.KakaoUser;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static com.studit.backend.domain.recruit.RecruitStatus.ACTIVE;
import static com.studit.backend.domain.recruit.RegisterStatus.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final StudyRecruitRepository studyRecruitRepository;
    private final StudyRegisterRepository studyRegisterRepository;
    private KakaoUser kakaoUser;

    public void checkUserNickName(String userNickName) {

        String kakaoNickname = kakaoUser.getKakao_account().getProfile().getNickname();

        // 닉네임과 조회했을때 다른 전체 유저중에 중복있을때 ->클라이언트쪽에서 메세지
        if (userRepository.findByNickname(kakaoUser.getKakao_account().getProfile().getNickname()).size() > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "유저 닉네임이 중복됩니다.");
        }
    }
    public Page<StudyRoom> getStudyStatusByUser(User user, Pageable pageable) {
        return studyRoomRepository.findAllByParticipant(user, pageable);
    }
    public Page<StudyRecruit> getStudyStatusByRecruit(User user) {
        //RecruitStatus->ACTIVE && RegisterStatus->REGISTER인
        //시작날짜도 오늘보다 전인
        Pageable pageable = PageRequest.of(0, 10);
        Page<StudyRecruit> result = studyRecruitRepository.findWaitingStudyRoomsByUser(
                user,REGISTER,ACTIVE,pageable);
        return result;
    }
    public Page<StudyRoom> getOngoingStudies(User user) {
        LocalDateTime today = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);
        return studyRoomRepository.findByStatusAndStudyStartAtAfter(
                StudyRoomStatus.PROGRESS, today, pageable);
    }
    public Page<StudyRoom> getCompletedStudies(User user) {
        LocalDateTime today = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);
        return studyRoomRepository.findByStatusAndStudyEndAtBefore(
                StudyRoomStatus.ENDED, today, pageable);
    }
}
