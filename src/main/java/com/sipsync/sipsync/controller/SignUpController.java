package com.sipsync.sipsync.controller;
import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.SingUpService;
import com.sipsync.sipsync.service.TokenService;
import org.antlr.v4.runtime.TokenSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignUpController {

    @Autowired private SingUpService singUpService;
    @Autowired private TokenService tokenService;

    // sign up user
    @ResponseBody
    @PostMapping("/api/signup")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
           User savedUser = singUpService.addUser(user.getEmail(), user.getPassword());
           String token = tokenService.genTokenAfterSignUp(savedUser.getId(), savedUser.getEmail());
           sendVerificationEmail(savedUser.getEmail(), token);

            return ResponseEntity.ok("Success.");
        } catch(Exception e) {
            return ResponseEntity.status(500).body("Server encountered an error");
        }
    }

    @ResponseBody
    @PostMapping("/sendVerificationEmail")
    public void sendVerificationEmail(@RequestParam String email,  @RequestParam String token){
        singUpService.sendVerificationEmail(email, token);
    }


    @ResponseBody
    @GetMapping("/verify")
    public void verifyEmail(@RequestParam("token")String token){
     singUpService.verifyUser(token);
    }
}
