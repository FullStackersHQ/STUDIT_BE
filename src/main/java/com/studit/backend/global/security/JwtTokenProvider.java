package com.studit.backend.global.security;

import com.studit.backend.global.config.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.studit.backend.domain.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final byte[] key;
    private CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = secretKey.getBytes(StandardCharsets.UTF_8);
    }

    // JWT 생성
    public String createToken(Long userId, User.Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 1일

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();
    }


    // JWT 검증 로직 추가
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new java.util.Date());
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            System.out.println("잘못된 JWT 토큰입니다.");
        } catch (SignatureException e) {
            System.out.println("JWT 서명이 유효하지 않습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 비어 있습니다.");
        }
        return false;
    }


    public String resolveToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 제거 후 토큰 반환
        }
        return null;
    }

    // 토큰에서 사용자명 추출
    public Long getUserIdFromToken(String token) {
        try {
            return Long.parseLong(
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject() // 토큰의 subject를 userId로 저장했으므로 가져오기
            );
        } catch (NumberFormatException e) {
            System.out.println("토큰에서 userId를 파싱하는 중 오류 발생: " + e.getMessage());
            return null; // userId를 정상적으로 변환할 수 없는 경우
        } catch (JwtException e) {
            System.out.println("JWT 파싱 오류: " + e.getMessage());
            return null;
        }
    }
    //토큰 만료 여부 체크
    public boolean isExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());  // 현재 시간보다 이전이면 true 반환
        } catch (JwtException e) {
            return true;
        }
    }
    public long getExpiration(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .getTime();
        } catch (JwtException e) {
            return 0L;
        }
    }


    public String getTokenType(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("type", String.class); // "type" 클레임에서 문자열 값 가져오기
        } catch (Exception e) {
            return null; // 토큰이 유효하지 않다면 null 반환
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(getUserIdFromToken(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}