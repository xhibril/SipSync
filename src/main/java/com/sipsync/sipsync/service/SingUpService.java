package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.repository.VerifyUserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
public class SingUpService {

    @Autowired UserRepository signUpRepo;
    @Autowired JavaMailSender mailSender;
    @Autowired VerifyUserRepository verifyRepo;


    // add user and return it
    public User addUser(String email, String password){

        User user = new User(email, password);
        signUpRepo.save(user);
        return user;
    }


    // send verification email

    public void sendVerificationEmail(String email, String token){
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        System.out.println(encodedToken);

        String link = "http://localhost:8080/verify?token=" + encodedToken;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Please clicking the following link to verify: ");
        message.setText(link);
        mailSender.send(message);

    }

    // rebuild token to verify user
    public void verifyUser(String token) {
        String secretKey = System.getenv("JWT_SECRET");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            Long id = claims.get("id", Long.class);
            String email = claims.get("email", String.class);
            verifyRepo.verifyById(id);

            System.out.print("Worked" + id +email);


        } catch (ExpiredJwtException e) {
            System.out.print("Expired");
        } catch (SignatureException e) {
            System.out.print("Invalid sig");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









}


