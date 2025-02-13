package com.studit.backend.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.studit.backend.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Base64 디코딩
        this.secretKey = Keys.hmacShaKeyFor(keyBytes); // Key 타입 변환
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
                .signWith(secretKey, SignatureAlgorithm.HS256) // ✅ Key 타입 사용
                .compact();
    }

    // HTTP 요청에서 JWT 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith((SecretKey) secretKey) // Key 타입 전달
                    .build()
                    .parseSignedClaims(token.trim());

            return !claims.getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 토큰이 만료됨");
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("JWT 토큰이 유효하지 않음");
        }
        return false;
    }

    // JWT 에서 userId 가져오기
    public UserDetails getUserDetails(String token) {
        String userId = Jwts.parser()
                .verifyWith((SecretKey) secretKey) // Key 타입 전달
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return org.springframework.security.core.userdetails.User.withUsername(userId).password("").roles("USER").build();
    }

    // JWT 에서 userId 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.valueOf(claims.getSubject());
    }

    // JWT 에서 Claims(페이로드) 추출
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}