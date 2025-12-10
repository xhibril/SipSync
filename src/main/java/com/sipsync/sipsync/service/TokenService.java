package com.sipsync.sipsync.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {


    public Long extractId(String token) {

        String secretKey = System.getenv("JWT_SECRET");

        // rebuild token and extract id from payload
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = claims.get("id", Long.class);

        return userId;

    }


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

