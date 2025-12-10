package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import com.sipsync.sipsync.repository.VerifyUserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.antlr.v4.runtime.Token;
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
    @Autowired TokenService tokenService;


    // add user and return it
    public void addUser(String email, String password){

      // "Not set" is last streak update date
        User user = new User(email, password, "Not set");

        // save the saved user for id
        User savedUser = signUpRepo.save(user);

        // generate token and send email
        String token = tokenService.genTokenAfterSignUp(savedUser.getId(), savedUser.getEmail());
        sendVerificationEmail(savedUser.getEmail(), token);
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

        } catch (ExpiredJwtException e) {
            return false;

        } catch (SignatureException e) {
            System.out.print("Invalid sig");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}


