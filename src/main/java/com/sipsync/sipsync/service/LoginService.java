package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.repository.VerifyUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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
    @Autowired VerifyUserRepository verifyRepo;



    public String isUserValid(String email, String password, HttpServletResponse res) {

        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getPassword().equals(password)) {
                // if details r successful gen token auth token and store it
                String token = genTokenAfterLogin(user.getId(), res);
                storeAuthToken(token, res);

                return "SUCCESS";
            }
        }
        return "FAIL";
    }








    public String genTokenAfterLogin(Long id, HttpServletResponse res) {

        String secret = System.getenv("JWT_SECRET");
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        String token = Jwts.builder()
                .setSubject("User")
                .claim("id", id)
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(key)
                .compact();

        return token;
    }


    // store auth token as http cookie
    public void storeAuthToken(String token, HttpServletResponse res){
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        res.addCookie(cookie);
    }









}
