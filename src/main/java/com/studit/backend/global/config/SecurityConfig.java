package com.studit.backend.global.config;

import com.studit.backend.domain.oauth.security.CustomOAuth2UserService;
import com.studit.backend.global.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {



       @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login","auth/callback","/","/auth/kakao-logout").permitAll()
                        .anyRequest().authenticated()
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
                );
        return http.build();
    }

//    @Autowired
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/login", "/home", "/auth/callback", "/auth/logout", "/auth/kakao-logout").permitAll()
//                        .requestMatchers("/secured/**").authenticated()
//                        .anyRequest().permitAll()
//                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/auth/login")  // 로그인 페이지를 설정
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/auth/logout")  // 로그아웃 URL을 설정
//                        //.logoutSuccessUrl("/auth/logout-success")  // 로그아웃 후 리디렉션할 URL 설정
//                        .logoutSuccessUrl("/auth/kakao-logout")  // 로그아웃 후 리디렉션할 URL 설정
//                        .invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }


}

