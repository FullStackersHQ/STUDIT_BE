package com.studit.backend.global.security;

import com.studit.backend.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import static com.studit.backend.domain.user.entity.User.Role.*;

@Component
public class JwtTokenProvider {
    private final byte[] key;

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

}

