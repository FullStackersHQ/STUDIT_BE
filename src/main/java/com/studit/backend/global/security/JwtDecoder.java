package com.studit.backend.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class JwtDecoder {


    private static byte[] keys;

    public JwtDecoder(@Value("${jwt.secret}") String secretKey) {
        this.keys = secretKey.getBytes(StandardCharsets.UTF_8);
    }

    private static SecretKey SECRET_KEY = Keys.hmacShaKeyFor(keys);

    public static String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userId").toString();
    }
}
