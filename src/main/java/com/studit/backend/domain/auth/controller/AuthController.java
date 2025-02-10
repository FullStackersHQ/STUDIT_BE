package com.studit.backend.domain.auth.controller;

import com.studit.backend.domain.user.entity.KakaoUser;
import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import com.studit.backend.global.security.JwtTokenProvider;
import com.studit.backend.domain.oauth.kakao.KakaoService;
import com.studit.backend.domain.oauth.kakao.KakaoTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
     *
     * @param
     * @return
     */
    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> kakaoCallback(@RequestParam("code") String code) {

        // access토큰 받기
        accessToken = kakaoTokenService.getKakaoToken(code);

        // 카카오 API에서 유저 정보 가져오기
        KakaoUser kakaoUser = kakaoService.getUserInfo(accessToken);

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

        //  JWT 발급
        String jwt = jwtTokenProvider.createToken(kakaoUser.getId(), accessToken);

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
    @GetMapping("/kakao-logout")
    public ResponseEntity<String> kakaoLogout() {
        String logoutUrl = "https://kauth.kakao.com/oauth/logout" +
                "?client_id=" + clientId +
                "&logout_redirect_uri=" + logoutRedirectUri;

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", adminKey);

        // HttpEntity에 헤더 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // RestTemplate을 이용해 GET 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(logoutUrl, HttpMethod.GET, entity, String.class);


        return ResponseEntity.status(response.getStatusCode()).body("카카오 로그아웃 완료");
    }

    String accessToken;


    /**
     * 카카오 회원탈퇴 -> access 토큰 필요. 혹은 어드민키
     * @param -> 안됀
     * @return
     */

  /*  @GetMapping("/kakao-logout")
    public ResponseEntity<String> kakaoLogoutGet() {

        //일단 본인것으로 테스트 해봄
        long userId=3911329676l;
        //User user = userRepository.findById(userId).get();
        //http://localhost:8080

        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink" +
                "?user_id=" + "3911329676" +
                "&&referrer_type=UNLINK_FROM_APPS";


    // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
       headers.set("Authorization", "KakaoAK "+adminKey);  // 카카오 API 키

      // HttpEntity에 헤더 추가
       HttpEntity<String> entity = new HttpEntity<>(headers);

     // RestTemplate을 이용해 GET 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(unlinkUrl, HttpMethod.GET, entity, String.class);

        // 로그아웃 완료 메시지 반환
        return ResponseEntity.status(response.getStatusCode()).body("카카오 로그아웃 완료");
    }

*/
    /**
     * 안됌
     */
  /*  @PostMapping("/kakao-logout")
    public ResponseEntity<String> kakaoLogout() {

        //public ResponseEntity<String> kakaoLogout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {


        // Authorization 헤더에서 "Bearer "를 제외한 토큰 값만 추출
        //String token = authHeader.replace("Bearer ", "");
*/
    // JWT 토큰 검증 (토큰 검증 로직 추가 필요)
//        boolean isTokenValid = jwtTokenProvider.validateToken(token);
//
//        if (!isTokenValid) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 토큰입니다.");
//        }else {
//
//            String unlinkUrl = "https://kapi.kakao.com/v1/user/logout";
//
//            // HTTP 요청 헤더 설정
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            headers.set("Authorization", "KakaoAK "+adminKey);
//            headers.set("Content-Type", "application/x-www-form-urlencoded");
//
//            //일단 본인것으로 테스트 해봄
//            long userId=3911329676l;
//            User user = userRepository.findById(userId).get();
//
//
//            // HTTP 요청 본문 설정
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("target_id_type", "user_id");
//            params.add("target_id", user.getId().toString());
//
//            // HTTP 요청 생성
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//            // HTTP 요청 보내기
//            RestTemplate restTemplate = new RestTemplate();
//
//            try {
//                ResponseEntity<String> response = restTemplate.exchange(unlinkUrl, HttpMethod.POST, request, String.class);
//                return ResponseEntity.status(response.getStatusCode()).body("카카오 회원탈퇴 완료");
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원탈퇴 실패: " + e.getMessage());
//            }
//
//        }
//    }


}


