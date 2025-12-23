package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.repository.EmailVerificationRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired UserRepository userRepo;
    @Autowired EmailVerificationRepository verifyRepo;
    @Autowired JavaMailSender mailSender;

    // send verification email
    @Async
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


    // check if user is logged in and get their id
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




    public void generateAuthTokenAfterSignup(Long userId, String email) {
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

        sendVerificationEmail(email, token);
    }


    public Long getUserIdByEmail(String email){
        return userRepo.findIdByEmail(email);
    }


    public Boolean checkVerificationStatusByEmail(String email){
     return userRepo.findIsVerifiedByEmail(email);
    }

    public Boolean checkVerificationStatusById(Long userId){
       return userRepo.findIsVerifiedById(userId);
    }





}


