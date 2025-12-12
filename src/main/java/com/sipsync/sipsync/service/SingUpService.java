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
import java.util.Optional;

@Service
public class SingUpService {

    @Autowired UserRepository signUpRepo;
    @Autowired VerifyUserRepository verifyRepo;
    @Autowired TokenService tokenService;
    @Autowired VerificationService verificationService;
    @Autowired UserRepository userRepo;


    // add user and return it
    public void addUser(String email, String password){

      // "Not set" is last streak update date
        User user = new User(email, password, "Not set");

        // save the saved user for id
        User savedUser = signUpRepo.save(user);

        // generate token and send email
        String token = tokenService.genTokenAfterSignUp(savedUser.getId(), savedUser.getEmail());
        verificationService.sendVerificationEmail(savedUser.getEmail(), token);
    }


    public Boolean checkIfUserExists(String email){
       Optional<String> doesUserExist = userRepo.findEmailByEmail(email);


       if(doesUserExist.isPresent()){
           return  true;
       } else {
           return false;
       }
    }
}


