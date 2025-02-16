package com.studit.backend.domain.user.controller;


import com.studit.backend.domain.user.repository.UserRepository;
import com.studit.backend.domain.user.service.UserService;
import com.studit.backend.global.config.CustomUserDetails;
import com.studit.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    //유저 닉네임 수정
    //@PreAuthorize("isAuthenticated()") -> 사용가능
    @PreAuthorize("hasAuthority('USER')") // -> 사용가능
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, String> response = new HashMap<>();
        response.put("유저id:",String.valueOf(userDetails.getUser().getId()));
        response.put("유저카카오id:",String.valueOf(userDetails.getUser().getKakaoId()));
        response.put("유저닉네임:",String.valueOf(userDetails.getUser().getNickname()));
        response.put("유저권한:",String.valueOf(userDetails.getUser().getRole()));
        response.put("message:","✅ 인증된 사용자입니다.");
        return ResponseEntity.ok(response);
    }


}



