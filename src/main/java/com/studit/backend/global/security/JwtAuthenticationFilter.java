package com.studit.backend.global.security;

import com.studit.backend.domain.user.entity.User;
import com.studit.backend.domain.user.repository.UserRepository;
import com.studit.backend.global.config.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtTokenProvider jwtTokenProvider;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        // HTTP 헤더에서 Authorization 값 가져오기
//        String token = jwtTokenProvider.resolveToken(request);
//        // 토큰이 존재하고 유효하면 인증 객체(SecurityContext) 설정
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            UserDetails userDetails = jwtTokenProvider.getUserDetails(token);
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        // 다음 필터로 이동
//        filterChain.doFilter(request, response);
//    }
//}
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String token = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            User user = userRepository.findById(userId).orElse(null);

            if (user != null) {
                CustomUserDetails userDetails = new CustomUserDetails(user);
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else {
                System.out.println("JWT가 유효하지 않음 또는 없음");
            }
        }

        filterChain.doFilter(request, response);
    }
}
