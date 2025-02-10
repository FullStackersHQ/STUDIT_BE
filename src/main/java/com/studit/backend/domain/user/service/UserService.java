package com.studit.backend.domain.user.service;

import com.studit.backend.domain.user.entity.KakaoUser;
import com.studit.backend.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    KakaoUser kakaoUser;

    public void checkUserNickName(String userNickName) {

        // 닉네임과 조회했을때 다른 전체 유저중에 중복있을때 ->클라이언트쪽에서 메세지
        if (userRepository.findByNickname(kakaoUser.getKakao_account().getProfile().getNickname()).size() > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "유저 닉네임이 중복됩니다.");
        }
    }
}
