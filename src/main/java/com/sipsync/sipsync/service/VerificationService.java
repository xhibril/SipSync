package com.sipsync.sipsync.service;

import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.repository.VerifyUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class VerificationService {

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


    // rebuild token to verify user
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
}

