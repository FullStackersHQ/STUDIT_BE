package com.studit.backend.global.config;

import com.studit.backend.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login","auth/callback","/","/auth/kakao-logout").permitAll()
                        .requestMatchers("/ws/timer").permitAll() // 웹소켓 경로도 허용
                        //.anyRequest().authenticated()
                        .anyRequest().permitAll()  // 모든 요청 허용
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/kakao-logout")
                        .invalidateHttpSession(true)  // 세션을 무효화
                        .clearAuthentication(true)  // 인증 정보 제거
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/");  // 로그인 후 홈 페이지로 리디렉션
                        })
                        .failureHandler((request, response, exception) -> {
                            response.sendRedirect("/login-fail");  // 로그인 실패 시 리디렉션
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 적용;

        return http.build();
    }

}

