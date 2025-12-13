package com.sipsync.sipsync.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {





    public String genTokenAfterSignUp(Long userId, String email) {

        String token;

        String secret = System.getenv("JWT_SECRET");
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("id", userId)
                .claim("email", email)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60* 60))
                .signWith(key)
                .compact();

        return token;
    }
}

