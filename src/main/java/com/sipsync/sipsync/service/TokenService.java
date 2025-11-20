package com.sipsync.sipsync.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class TokenService {


    public Long extractId(String token){

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
}
