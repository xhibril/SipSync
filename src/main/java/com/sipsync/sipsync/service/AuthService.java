package com.sipsync.sipsync.service;

import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.repository.VerifyUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepo;
    @Autowired
    VerifyUserRepository verifyRepo;

    @Autowired
    JavaMailSender mailSender;


    public Boolean isUserVerified(String email) {
        return userRepo.findUserByEmail(email);
    }

    // send verification email
    public void sendVerificationEmail(String email, String token){
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        String link = "http://localhost:8080/email/check-token?token=" + encodedToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Please clicking the following link to verify: ");
        message.setText(link);
        mailSender.send(message);
    }


    // rebuild token to verify user email
    public Boolean verifyUser(String token) {

        String secretKey = System.getenv("JWT_SECRET");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            Long id = claims.get("id", Long.class);
            verifyRepo.verifyById(id);

        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public Long getAuthenticatedUserId(HttpServletRequest req){
        // get token from cookie
        String token = checkIfAuthTokenExists(req);
        if (token == null) return null;

        // extract id from token
        Long userId = extractId(token);
        if(userId == null) return null;

        // check if that user id is valid
        Boolean isUserIdValid = userRepo.existsById(userId);

        if(isUserIdValid){
            return userId;
        } else {
            return null;
        }
    }


    public String checkIfAuthTokenExists(HttpServletRequest req){
        // get all the cookies sent by the browser
        Cookie[] cookies = req.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if("authToken".equals(cookie.getName())){
                    // Get the value of that cookie
                    String token = cookie.getValue();
                    return token;
                }
            }
        }
        return null;
    }



    public Long extractId(String token) {
        String secretKey = System.getenv("JWT_SECRET");
        Long userId = null;

        // rebuild token and extract id from payload
        try {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

         userId = claims.get("id", Long.class);

        } catch (Exception e ){
            return null;
        }
        return userId;
    }












}

