package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired UserRepository userRepo;

    // check if user credentials are correct
    public Boolean isUserValid(String email, String password, Boolean rememberMe, HttpServletResponse res) {
        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getPassword().equals(password)) {
                // set expiration
                Long length = expiration(rememberMe);

                // if details r successful gen token auth token and store it
                String token = generateAuthToken(user.getId(), length);
                storeAuthToken(token, Math.toIntExact(length / 1000), res);
                return true;
            }
        }
        return false;
    }


    // generate auth token
    public String generateAuthToken(Long id, Long length) {

        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET is missing or too short");
        }

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        String token = Jwts.builder()
                .setSubject("User")
                .claim("id", id)
                .setExpiration(new Date(System.currentTimeMillis() + length))
                .signWith(key)
                .compact();

        return token;
    }


    // store auth token as http cookie
    public void storeAuthToken(String token, int length, HttpServletResponse res) {
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        //  cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(length); // expires in 7 days
        res.addCookie(cookie);

    }

    private Long expiration(Boolean rememberMe) {
        final long TWO_WEEKS = 1000L * 60 * 60 * 24 * 14;
        final long TWO_HOURS = 1000L * 60 * 60 * 2;
        return rememberMe ? TWO_WEEKS : TWO_HOURS;
    }

    public Boolean logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //  cookie.setSecure(true);
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        return true;
    }
}

