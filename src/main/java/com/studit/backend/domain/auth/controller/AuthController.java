package com.studit.backend.domain.auth.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.studit.backend.domain.user.entity.KakaoUser;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import com.studit.backend.global.config.CustomUserDetails;
import com.studit.backend.global.security.JwtTokenProvider;
import com.studit.backend.domain.oauth.kakao.KakaoService;
import com.studit.backend.domain.oauth.kakao.KakaoTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoTokenService kakaoTokenService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.service_app_admin_key}")
    private String adminKey;

    @Value("${kakao.logout_redirect_uri}")
    private String logoutRedirectUri;

    /**
     * 인가(Authorization)코드 받기
     *
     * @return 카카오 콜백으로 인가코드 발급완료
     */
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/auth/authorize");
    }
    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize() {
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
     *
     * @param
     * @return
     */
    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam("code") String code) {

        // access토큰 받기
        String accessToken = kakaoTokenService.getKakaoToken(code);

        // 카카오 API에서 유저 정보 가져오기
        KakaoUser kakaoUser = kakaoService.getUserInfo(accessToken);

        // DB에 해당 유저가 있는지 확인
        User userInfo = userRepository.findById(kakaoUser.getId())
                .orElseGet(() -> { //없으면
                    User newUser = new User(
                            kakaoUser.getId(),
                            kakaoUser.getKakao_account().getProfile().getNickname(),
                            kakaoUser.getKakao_account().getProfile().getProfile_image_url(),
                            User.Role.ROLE_USER
                    );
                    return userRepository.save(newUser);
                });

        //DB에 해당 유저 정보가 있다면 -> 만들어야할 이동시켜야함 마이페이지로
        User user = userRepository.findByKakaoId(userInfo.getKakaoId());

        //  JWT 발급
        String jwt = jwtTokenProvider.createToken(user.getId(), user.getRole());
        // 권한 정보 추가
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(User.Role.ROLE_USER.name()));
        //SecurityContextHolder에 인증 정보 저장
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 응답에 JWT 포함
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인 성공");
        response.put("jwt-token", jwt);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(response);

    }


    /**
     * 카카오 연결해제  -> 페이지는 넘어간다만.. 근데 JWT토큰 처리는 또 따로 해줘야되고. DB에서도 ID로 정보 지워줘야함
     *
     * @return
     */
    @Secured("USER")
    @GetMapping("/kakao-logout")
    public void kakaoLogout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        String kakaoLogoutUrl = "https://kauth.kakao.com/oauth/logout" +
                "?client_id=" + clientId +
                "&logout_redirect_uri=" + logoutRedirectUri;

        this.unlinkKakaoAccount(adminKey, String.valueOf(userDetails.getUser().getKakaoId()));
        // 2. 카카오 로그아웃 URL 호출
        ResponseEntity<String> restResponse = restTemplate.exchange(kakaoLogoutUrl, HttpMethod.GET, null, String.class);
        // 3. DB에서 사용자 삭제
        if (restResponse.getStatusCode().is2xxSuccessful()) {
            userRepository.deleteById(userDetails.getUser().getId());
            // JWT 토큰 만료 처리 (Redis에 로그아웃된 토큰 저장)
            String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
            long expiration = jwtTokenProvider.getExpiration(jwtToken);
            redisTemplate.opsForValue().set(jwtToken, "logout", expiration, TimeUnit.MILLISECONDS);
        }
        response.sendRedirect("http://localhost:8080/api/auth/nextwork");
    }
    public void unlinkKakaoAccount(String adminKey, String kakaoUserId) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", kakaoUserId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(unlinkUrl, HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("✅ 카카오 계정 연결 해제 성공!");
        } else {
            System.out.println("❌ 카카오 계정 연결 해제 실패: " + response.getBody());
        }
    }

    @GetMapping("/nextwork")
    public ResponseEntity<Map<String, String>> next() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "다음 작업");
        return ResponseEntity.ok()
                .body(response);
    }

}


