package com.sipsync.sipsync.controller;

import com.sipsync.sipsync.model.User;
import com.sipsync.sipsync.service.SingUpService;
import com.sipsync.sipsync.service.TokenService;
import com.sipsync.sipsync.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class VerificationController {

    @Autowired private SingUpService singUpService;
    @Autowired private TokenService tokenService;
    @Autowired private VerificationService verificationService;

    // load verification page
    @GetMapping("/verify")
    public String verifyPage(){
        return "VerifyPage";
    }


    // send verification link to user
    @ResponseBody
    @PostMapping("/email/send-token")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody User user){

        try {
            String token = tokenService.genTokenAfterSignUp(user.getId(), user.getEmail());
            verificationService.sendVerificationEmail(user.getEmail(), token);
            return ResponseEntity.ok("Success.");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Server encountered an error.");
        }
    }

    // verify user email
    @GetMapping("/email/check-token")
    public String verifyUser(@RequestParam("token")String token){
        Boolean verified = verificationService.verifyUser(token);
        if(verified){
            return "redirect:/login?verified=true";
        } else {
            return "redirect:/login?verified=false";
        }
    }


    // check if user is verified after logging in
    @ResponseBody
    @GetMapping("/api/verification-status")
    public Boolean isUserVerified(@RequestParam String email) {
        return verificationService.isUserVerified(email);
    }


}
