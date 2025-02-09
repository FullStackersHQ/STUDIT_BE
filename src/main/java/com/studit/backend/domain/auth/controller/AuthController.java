package com.studit.backend.domain.auth.controller;
import com.studit.backend.domain.user.entity.Role;
import com.studit.backend.domain.user.entity.kakaoUser;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import com.studit.backend.global.security.JwtTokenProvider;
import com.studit.backend.domain.oauth.kakao.KakaoService;
import com.studit.backend.domain.oauth.kakao.KakaoTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoTokenService kakaoTokenService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 인가(Authorization)코드 받기
     * @return 카카오 콜백으로 인가코드 발급완료
     */
    @GetMapping("/login")
        public ResponseEntity<Void> Login() {
            String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize" +
                    "?client_id=" + clientId +
                    "&redirect_uri=" + redirectUri +
                    "&response_type=code";

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, kakaoAuthUrl)
                    .build();
    }

    /**
     * 인가코드로 access 토큰 받기 -> 유저정보 가져오기 -> JWT 토큰 생성
     * @param
     * @return
     */

    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam("code") String code) {

        // access토큰 받기
        String accessToken = kakaoTokenService.getKakaoToken(code);


        // 카카오 API에서 유저 정보 가져오기
        kakaoUser kakaoUser = kakaoService.getUserInfo(accessToken);

        // DB에 해당 유저가 있는지 확인
            User userInfo = userRepository.findById(kakaoUser.getId())
                    .orElseGet(() -> { //없으면
                        User newUser = new User(
                                kakaoUser.getId(),
                                kakaoUser.getKakao_account().getProfile().getNickname(),
                                kakaoUser.getKakao_account().getProfile().getProfile_image_url()
                        );
                        return userRepository.save(newUser);
                    });


        User user = userRepository.findById(kakaoUser.getId())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        Role role = user.getRole();

        //  JWT 발급
        String jwt = jwtTokenProvider.createToken(kakaoUser.getId(), role.name());

        // 응답에 JWT 포함
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인 성공");
        response.put("jwt-token", jwt);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(response);

    }



}


