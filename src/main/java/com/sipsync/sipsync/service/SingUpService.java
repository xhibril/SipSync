package com.sipsync.sipsync.service;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SingUpService {

    @Autowired TokenService tokenService;
    @Autowired AuthService authService;
    @Autowired UserRepository userRepo;

    // add user and return it
    public Boolean addUser(String email, String password){
        // check if user it not already registered
        if(!(userRepo.existsByEmail(email))){
            User user = new User(email, password, null);   // null is last streak update date

            // save the saved user for id
            User savedUser = userRepo.save(user);

            // generate token and send email
            String token = tokenService.genTokenAfterSignUp(savedUser.getId(), savedUser.getEmail());
            authService.sendVerificationEmail(savedUser.getEmail(), token);
            return true;
        } else {
            return false;
        }
    }
}


